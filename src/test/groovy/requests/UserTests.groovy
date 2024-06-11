import io.restassured.response.Response
import org.junit.jupiter.api.Test
import groovy.json.JsonSlurper
import resources.HTTPClient
import resources.TestDataBuilder
import resources.ToDo
import resources.Users

import static io.restassured.RestAssured.*

class UserTests {

    private static final String BASE_URL = 'https://jsonplaceholder.typicode.com'
    private JsonSlurper jsonSlurper = new JsonSlurper()
    private static final int MAX_RETRIES = 3
    private static final int RETRY_DELAY_MS = 1000

    @Test
    void createUserAndValidate() {
        // Step 1: Create a test user
        int userId = 11  // Simulate the scenario where user with userId 11 cannot be saved
        Response userResponse = retry(MAX_RETRIES) { Users.createUser(userId) }

        if (userResponse == null || userResponse.statusCode() != 201) {
            println("Failed to create user with ID $userId")
            return
        }

        def user = jsonSlurper.parseText(userResponse.body().asString())
        if (user.id != userId) {
            println("User ID does not match the expected value. Expected: $userId, Actual: ${user.id}")
            return
        }

        // Step 2: Create associated entities (posts, comments, albums, photos, todos)
        def post = createPost(user.id)
        createComment(post.id)
        def album = createAlbum(user.id)
        createPhoto(album.id)
        ToDo.createTodoForUser(user.id)

        // Step 3: Validate integrity (simulate DB issues)
        validateUser(user.id)
    }

    private def createPost(int userId) {
        def postPayload = TestDataBuilder.createPostPayload(userId)
        Response postResponse = retry(MAX_RETRIES) { HTTPClient.post("/posts", postPayload as String) }
        return jsonSlurper.parseText(postResponse.body().asString())
    }

    private def createComment(int postId) {
        def commentPayload = TestDataBuilder.createCommentPayload(postId)
        retry(MAX_RETRIES) { HTTPClient.post("/comments", commentPayload as String) }
    }

    private def createAlbum(int userId) {
        def albumPayload = TestDataBuilder.createAlbumPayload(userId)
        Response albumResponse = retry(MAX_RETRIES) { HTTPClient.post("/albums", albumPayload as String) }
        return jsonSlurper.parseText(albumResponse.body().asString())
    }

    private def createPhoto(int albumId) {
        def photoPayload = TestDataBuilder.createPhotoPayload(albumId)
        retry(MAX_RETRIES) { HTTPClient.post("/photos", photoPayload as String) }
    }

    private void validateUser(int userId) {
        // Simulate checking user and associated resources for duplicates or missing entries
        Response userResponse = retry(MAX_RETRIES) { Users.getUserById(userId) }
        if (userResponse == null || userResponse.statusCode() != 200) {
            println("User with ID $userId not found during validation")
            return
        }

        def user = jsonSlurper.parseText(userResponse.body().asString())
        assert user.id == userId

        def postsResponse = retry(MAX_RETRIES) { HTTPClient.get("/posts?userId=$userId") }
        assert postsResponse.statusCode() == 200

        def posts = jsonSlurper.parseText(postsResponse.body().asString())
        if (posts.size() != 1) {
            println("Expected 1 post for user with ID $userId, but found ${posts.size()}")
            return
        }

        def commentsResponse = retry(MAX_RETRIES) { HTTPClient.get("/comments?postId=${posts[0].id}") }
        assert commentsResponse.statusCode() == 200

        def comments = jsonSlurper.parseText(commentsResponse.body().asString())
        if (comments.size() != 1) {
            println("Expected 1 comment for post with ID ${posts[0].id}, but found ${comments.size()}")
            return
        }

        def albumsResponse = retry(MAX_RETRIES) { HTTPClient.get("/albums?userId=$userId") }
        assert albumsResponse.statusCode() == 200

        def albums = jsonSlurper.parseText(albumsResponse.body().asString())
        if (albums.size() != 1) {
            println("Expected 1 album for user with ID $userId, but found ${albums.size()}")
            return
        }

        def photosResponse = retry(MAX_RETRIES) { HTTPClient.get("/photos?albumId=${albums[0].id}") }
        assert photosResponse.statusCode() == 200

        def photos = jsonSlurper.parseText(photosResponse.body().asString())
        if (photos.size() != 1) {
            println("Expected 1 photo for album with ID ${albums[0].id}, but found ${photos.size()}")
            return
        }

        def todosResponse = retry(MAX_RETRIES) { HTTPClient.get("/todos?userId=$userId") }
        assert todosResponse.statusCode() == 200

        def todos = jsonSlurper.parseText(todosResponse.body().asString())
        if (todos.size() != 1) {
            println("Expected 1 todo for user with ID $userId, but found ${todos.size()}")
            return
        }
    }

    @Test
    void performanceTest() {
        long startTime = System.currentTimeMillis()

        100.times { index ->
            Response userResponse = retry(MAX_RETRIES) { Users.createUser(index) }
            if (userResponse != null && userResponse.statusCode() == 201) {
                def user = jsonSlurper.parseText(userResponse.body().asString())
                ToDo.createTodoForUser(user.id)
            } else {
                println("Failed to create user with index $index")
            }
        }

        long endTime = System.currentTimeMillis()
        long duration = endTime - startTime
        println("Performance test duration: ${duration} ms")
    }

    private <T> T retry(int retries, Closure<T> closure) {
        int attempt = 0
        while (attempt < retries) {
            try {
                return closure.call()
            } catch (Exception e) {
                attempt++
                if (attempt >= retries) {
                    throw e
                }
                Thread.sleep(RETRY_DELAY_MS)
            }
        }
        return null
    }
}

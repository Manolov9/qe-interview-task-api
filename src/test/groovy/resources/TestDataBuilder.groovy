package resources

class TestDataBuilder {

  static Map createUserPayload(int userId) {
    return [
            id: userId,
            name: 'John Doe',
            username: 'johndoe',
            email: 'johndoe@example.com',
            address: [
                    street: 'Kulas Light',
                    suite: 'Apt. 556',
                    city: 'Gwenborough',
                    zipcode: '92998-3874',
                    geo: [
                            lat: '-37.3159',
                            lng: '81.1496'
                    ]
            ],
            phone: '1-770-736-8031 x56442',
            website: 'hildegard.org',
            company: [
                    name: 'Romaguera-Crona',
                    catchPhrase: 'Multi-layered client-server neural-net',
                    bs: 'harness real-time e-markets'
            ]
    ]
  }

  static Map createPostPayload(int userId) {
    return [
            title: 'Test Post',
            body: 'This is a test post.',
            userId: userId
    ]
  }

  static Map createCommentPayload(int postId) {
    return [
            postId: postId,
            name: 'Test Comment',
            email: 'testcomment@example.com',
            body: 'This is a test comment.'
    ]
  }

  static Map createAlbumPayload(int userId) {
    return [
            title: 'Test Album',
            userId: userId
    ]
  }

  static Map createPhotoPayload(int albumId) {
    return [
            albumId: albumId,
            title: 'Test Photo',
            url: 'http://placehold.it/600/92c952',
            thumbnailUrl: 'http://placehold.it/150/92c952'
    ]
  }

  static Map createTodoPayload(int userId) {
    return [
            title: 'Test Todo',
            completed: false,
            userId: userId
    ]
  }
}

package resources

class ToDo {

  static void createTodoForUser(int userId) {
    def todoPayload = TestDataBuilder.createTodoPayload(userId)
    HTTPClient.post("/todos", todoPayload as String)
  }
}

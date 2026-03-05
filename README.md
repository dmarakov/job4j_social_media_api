Project goal: Develop a RESTful API for a social media platform that allows users to register, log in, create posts, chat, follow other users, and view their activity feed.

Requirements:

1.  Authentication and authorisation:

- Users can register by providing a username, email address, and password.

- Users can log in by providing the correct credentials.

    - The API must ensure the privacy of user data, including password hashing and the use of JWT.

1.  Post management:

- Users can create new posts by entering text, a title, and attaching images.

- Users can view other users' posts.

- Users can update and delete their own posts.

2. User interaction:

- Users can send friend requests to other users. From that moment on, the user who sent the request remains a subscriber until they unsubscribe themselves. If the user who received the request accepts it, both users become friends. If they decline, the user who sent the request remains a subscriber, as mentioned above.

- Users who are friends are also subscribers to each other.

- If one friend deletes the other from their friends list, they also unsubscribe. The second user must remain a subscriber.

    - Friends can send messages to each other (no chat implementation is required; users can request correspondence using a request).

3.  Subscriptions and activity feed:

- The user's activity feed should display the latest posts from the users they subscribe to.

    - The activity feed should support pagination and sorting by post creation time.

4. Error handling:

- The API should handle and return clear error messages in case of incorrect requests or internal server issues.

- The API should validate the entered data and return informative messages in case of incorrect format.

5. API documentation:

- The API should be well documented using tools such as Swagger or OpenAPI.

- The documentation should contain descriptions of available endpoints, request and response formats, and authentication requirements.

Technologies and tools:

- Programming language: Java

- Framework: Spring (Spring Boot is recommended)

- Database: PostgreSQL or MySQL is recommended

- Authentication and authorisation: Spring Security

- API documentation: Swagger or OpenAPI
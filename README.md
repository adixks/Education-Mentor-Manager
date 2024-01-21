# Kursy - Educational Application 🚀

Thank you for your interest in my application #Kursy! Below you will find a brief overview of features and highlights that distinguish my solution.

## Tools and Components 🔧
[Java 17, Spring Boot 3, Spring Validation, JPA, REST API, MySQL, Liquibase, CI/DI (CircleCi), Lombock, JUnit 5, Mockito 5, Integration Tests Faker, JaCoCo, Pagination, Profiles for Dev and Prod]

## Application Structure 🏗️

My application is solidly built on a three-tier structure, providing clarity and efficiency:

- **Controller (REST)**: Handles requests and delivers responses in an elegant way.
- **Service**: The place where the magic of business logic happens.
- **Repository**: Connects our application with the database, making everything work like clockwork.

Each defined entity goes through its unique three-tier process.

## Features that Challenge 🚀

### Teachers

- 📚 Fetching all teachers
- 🔄 Fetching a teacher by ID
- ➕ Adding a new teacher
- 🛠️ Partial update of a teacher - now changes in languages are a piece of cake!
- 🗑️ Deleting a teacher (soft delete)

### Students

- 📚 Fetching all students
- 🔄 Fetching a student by ID
- ➕ Adding a new student - protected against incorrect assignment of a teacher!
- 🛠️ Partial update of a student - now with a change of teacher
- 🗑️ Deleting a student (soft delete)

### Lessons

- 📚 Fetching all lessons
- 🔄 Fetching a lesson by ID
- ➕ Adding a new lesson - protected against planning in the past and overlapping with other lessons
- 🛠️ Update of the lesson date - applies only to lessons that have not yet started
- 🗑️ Deleting a lesson - applies only to lessons that have not yet started

## Additional Features that Distinguish Us 🌟

- 🔄 Controller Actions: Precisely defined actions for each endpoint, facilitating navigation and understanding.
- 🔄 Integration with CircleCi: Automation of the build and test process, guaranteeing high code quality.
- 🔄 Docker Support: Our application is ready to run in a Docker container.
- 🔄 Unit and Integration Tests with JUnit 5 and Mockito 5: We provide solid testing to avoid unpleasant surprises.
- 🔄 Faker for Controllers, Services, and Mappers: We create test data in a quick and efficient way.
- 🔄 Validations with Custom Annotations: Ensuring data correctness at every stage.
- 🔄 Pagination and Liquibase: Care for performance and data freshness.
- 🔄 JaCoCo Code Coverage: We monitor code coverage to maintain a high standard of quality. Report available after each merge to the head branch.
- 🔄 Profiles for Dev and Prod: Flexibility of configuration in different environments.

## How I got started 🚀.

My application is the result of my work with the future of education in mind. Hopefully my innovative solutions will improve more than one course activity.

Thank you for your attention and I look forward to presenting #Courses more closely! 🌐💡

Best regards,
## Adrian 👋

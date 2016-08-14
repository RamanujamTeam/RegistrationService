# RegistrationService

Technologies that should be used in this project:
1. maven
2. maven-jacoco-plugin with no less than 85% coverage and embedded in your main project <build> section
3. maven-checkstyle-plugin
4. travis-ci synchronized with your github project
5. Spring Boot and Spring JMS
6. HSQLDB embedded in service during the start and used as the main database
7. ActiveMQ as JMS Broker
8. Hibernate Validator library
9. Thymeleaf
10. Skeleton (instead of Bootstrap)

You need to build a microservice for users registration. Navigate to http://localhost:8080/registration

![Screenshot1](https://raw.githubusercontent.com/RamanujamTeam/RegistrationService/master/src/main/resources/img/1.png)

You can see in the right corner a lonely button "Sign Up" from Skeleton CSS framework that will still be at the right on mobile device.

After clicking this button the page should not reload (single page design) and the following registration form will appear:

![Screenshot2](https://raw.githubusercontent.com/RamanujamTeam/RegistrationService/master/src/main/resources/img/2.png)

Enter our email and password. The password should be validated based on Bean Validation rules with the help of Hibernate Validator library. It means it will be validated based on:

1. Classic email regexp.
2. **Should contain no less than 2 symbols and a "!" sign.**

Click **Create my account**.
At this moment an HSQLDB database is already created with information from user:

| email (VARCHAR)    | password (VARCHAR) | is_confirmed (BOOLEAN) |
|--------------------|--------------------|------------------------|
| my_email@gmail.com | baeldung_knight!32 |          false         |

Remember HSQLDB is an in-memory database, so all the data should be deleted after shutdown.

So, we have the user's data and the user sees a "success" screen:

![Screenshot3](https://raw.githubusercontent.com/RamanujamTeam/RegistrationService/master/src/main/resources/img/3.png)

"Check your email" means that a confirmation email should be delivered to users **real** email inbox. Use ActiveMQ broker and Spring JMS (there are plenty of tutorials on the web) to send a pretty HTML email to user. 

![Screenshot4](https://raw.githubusercontent.com/RamanujamTeam/RegistrationService/master/src/main/resources/img/4.png)

The user will receive a letter saying that he has successfully registered with this email address and a password that is slightly disguised (shows only 2 last symbols).

User clicks on the link. Pay attention that the link has "/confirm" mapping and some encrypted number of chars that actually contains information about his email and password so that the service can check if the user with these credentials exists in database and set the "is_confirmed" flag to true:

| email (VARCHAR)    | password (VARCHAR) | is_confirmed (BOOLEAN) |
|--------------------|--------------------|------------------------|
| my_email@gmail.com | baeldung_knight!32 |          true          |


**That's it!**
After the service changed the "is_confirmed" flag to true the user is redirected to /success routes:
![Screenshot5](https://raw.githubusercontent.com/RamanujamTeam/RegistrationService/master/src/main/resources/img/5.png)

where a long-waited link to video https://www.youtube.com/watch?v=dQw4w9WgXcQ will be waiting for him.

Let's conclude:
* maven builds up your project and libraries
* jacoco checks your test coverage
* checkstyle checks your code style
* spring boot sets up your project with predefined settings
* hibernate validator with a help of annotations checks users input
* hsqldb is embedded in-memory database that starts up with spring boot. If the service fails the data it destroyed.
* activemq is a message broker that with a special settings and a Spring JMS support will send an html email and information to a real email address.
* skeleton will make your site look nice
* thymeleaf is a view-template
* travis-ci will your pull requests for tests and checkstyle



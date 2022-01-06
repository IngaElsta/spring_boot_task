# spring_boot_task
0 Create a GitHub account (if not done yet). Create an open Git repository there. Share with the team.
1.
Use master or develop branch for integration and separate branch
for each of developers
2.
Use meaningful commit messages
3.
Complete tasks features iteratively, 1 feature = 1 commit.

1
Create a Spring Boot service (use Java and maven).
1.
https://start.spring.io/ can be used for quick creation

2
Create at least 2 simple endpoints (GET, POST) per person. (Use JSON type)
1.
Try to decompose your logic so that it can be described by several
classes/architecture layers. Do not write all of your code in the same
class.
2.
Try to use both query and path parameters.
3.
Validation of input params/POST body is required.
4.
Test your endpoints with Postman or other REST clients.

3
Inside of GET endpoint create scheduled CRON job to fetch some data and cache it. (For example, every 3 minutes your services
se nds a request to one of provided APIs)
* If your cache is empty call extarnal API. If cache is not empty get data from cache.
* Make cron schedule configurable from properties file
1.
Use Spring solutions for caching and scheduled job
2.
Use one of these APIs:
1.
https://aviationstack.com/
2.
https://fcsapi.com/
3.
https://openweathermap.org/

4
POST endpoint should consume some body and store data.
* Create simple entity class with auto generated key
* Create simple spring repository to save your entity
* Use in memory H2 DB to save your data
* Configure console access to H2 (make sure you can access it from

5
Cover your functionality with unit and integration tests.

6
Create swagger file for your service.

7
Take care of proper error handling.
* In case of invalid input params service should provide meaningful

8
Add proper logging to your functionality.
* Optionally you can write logs into

9
Be ready to present your service.
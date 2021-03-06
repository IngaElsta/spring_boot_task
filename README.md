# spring_boot_task
This API is meant as a backend for some sort of webpage that uses map as a way to choose location to plan
a some outdoor activity there on some date.
Checking if this is a place where any outdoor activity is actually possible or if the weather conditions 
are OK for that is out of the scope.

For GET endpoint it returns a limited weather prognosis.

For POST endpoints it tries to plan outdoor activities for specific location and depending on endpoint 
inform/refuse if any weather alert is expected in that day. Might add check if there is something planned already for that date and refuse in this case.

There are simple endpoints to retrieve a list of existing plans and delete a plan by id.

For more meaningful implementation of caching a favorite location service has been created as well.

0. Create a GitHub account (if not done yet). Create an open Git repository there. Share with the team.
* Use master or develop branch for integration and separate branch
for each of developers
* Use meaningful commit messages
* Complete tasks features iteratively, 1 feature = 1 commit.

1. Create a Spring Boot service (use Java and maven).
* https://start.spring.io/ can be used for quick creation
2. Create at least 2 simple endpoints (GET, POST) per person. (Use JSON type)
* Try to decompose your logic so that it can be described by several
classes/architecture layers. Do not write all of your code in the same
* Try to use both query and path parameters.
* Validation of input params/POST body is required.
* Test your endpoints with Postman or other REST clients.

3. Inside of GET endpoint create scheduled CRON job to fetch some data and cache it. (For example, every 3 minutes your services
se nds a request to one of provided APIs)
* If your cache is empty call external API. If cache is not empty get data from cache.
* Make cron schedule configurable from properties file
* Use Spring solutions for caching and scheduled job
* Use one of these APIs:

https://aviationstack.com/

https://fcsapi.com/

https://openweathermap.org/

4. POST endpoint should consume some body and store data.
* Create simple entity class with auto generated key
* Create simple spring repository to save your entity
* Use in memory H2 DB to save your data
* Configure console access to H2 (make sure you can access it from

5. Cover your functionality with unit and integration tests.

6. Create swagger file for your service.

7. Take care of proper error handling.
* In case of invalid input params service should provide meaningful

8. Add proper logging to your functionality.
* Optionally you can write logs into

9. Be ready to present your service.

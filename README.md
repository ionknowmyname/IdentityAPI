

**Identity API - JAVA 11 & 17 - APACHE MAVEN - POSTGRES**

Service can:

1. Sign up a user
2. Sending otp to email and phone number using maildev & Twilio
3. Activate user by validating email or phone number
4. Generate JWT upon logging in App User


master branch is Java 17

java11 branch is Java 11

**master branch contains**:<br /><br />
    - java 17 implementation <br />
    - does not send validation email or otp with twilio <br />
    - logs in the app user and generates JWT <br />
    - all requests without JWT in header to authenticated routes bounce you <br />



**java11 branch contains**: <br /><br />
    - java 11 implementation <br />
    - sends validation email with maildev and sends otp to phone number with twilio <br />
    - does not login a user and generate JWT <br />

Use mail dev as email server to test. <br />
    
    npm i -g maildev
to start maildev, enter "maildev" in terminal <br />
    enter http://localhost:1080/ in browser, email view is there
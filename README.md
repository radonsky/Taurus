##Taurus
###About

Taurus is a backend service for [Scorepio](https://play.google.com/store/apps/details?id=com.rbw.scorepio.activity) Android app.

It was developed specifically as a server part of Scorepio, 
but it is also an example of a simple RESTful service implemented in Scala using Play! framework.

It demonstrates the following:

 * How to use Play! to handle JSON requests and responses
 * How to store data in PostgreSQL currently using Anorm
 * How to handle BASIC HTTP-authentication in Play!
 * How to provide GZIP compression
 * and more...

The code is deployed on Heroku where it runs 24/7.

###Requirements

Taurus requires a few things to run:

1. [PostgreSQL](http://www.postgresql.org/) database
2. Java JRE 6 or newer 
3. [Play! Framework](http://www.playframework.com/) version 2.1+

###Environment

Taurus requires 2 environment variables to be set:

 * `DATABASE_URL` - JDBC URL to the DB used. It has to be complete URL to an empty database including user credentials.
 * `TAURUS_HTTP_BASIC_PASSWORD` - This is a password that is required for all authenticated requests.

###Running

After you set the 2 environment variables, you can go in the the directory where you cloned this project and run the following command:
    
    play run

This will run Taurus in the development mode.

###Contact
If you have any questions, please send me an email to my name at gmail.com.

###License

Copyright 2013 Marek Radonsky

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License. You may obtain a copy of the License at [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0) Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.

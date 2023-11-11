# RESTful Web-store
____
![spring](https://img.shields.io/badge/SpringBoot-V_3.15-green) ![rabbitMq](https://img.shields.io/badge/RabbitMQ-V_3.12.8-orange) ![Java](https://img.shields.io/badge/Java-17-orange)
Welcome to the documentation of ***RESTful Web-Store***. This project provides a programming interface for interacting with the store through an API. With it, you can retrieve information about products, place orders, and much more.
#### ****If you want to check API on [Heroku](https://dashboard.heroku.com) DM me in [TG](https://t.me/primeking666)****
## Installation
#### 1.
Clone the repository github.com/Sjuice1/RESTftulSN.git
```git 
git clone github.com/Sjuice1/RESTftulSN.git
```
#### 2.
Install latest version of [RabbitMQ](https://www.rabbitmq.com/download.html) and create [required](https://docs.google.com/document/d/1mXX92qcLs0WiYxFR642nrqCbJezkPlN_YU0ni4xXqtw/edit?usp=sharing) exchanges and queues.
#### 3.
Ensure the proper configuration of your database and [JMS](https://en.wikipedia.org/wiki/Jakarta_Messaging) by modifying the properties file. Locate the `application.properties` file in your project and update the relevant settings for your connection.

## Usage
To interact with this project, you can use the following commands. 

Each command includes an annotation that provides an explanation of its intended use.
#### Example
```java
@RequestMapping("/api/item")
@RestController


    ///////////////////////
    ////Get item reviews by item id
    ///////////////////////
    @GetMapping("{id}/reviews")
```
#### Or
```java
@RequestMapping("/api/item")
@RestController


    ///////////////////////
    ////Add new item using JSON like ItemDTO
    ///////////////////////
    @PostMapping("/add")
```
## Authentication
To authenticate users, create a user table in your database and adjust it in your code. In my schema, it looked like this:
![user](https://i.imgur.com/S6aTvHY.jpg)
[Other tables](https://imgur.com/a/Dx469AD).Be carefull, tables was changed by progression of project,so you can edit them as you like.

****Then**** ***change your role manually to admin and test API.***

About roles:

***Guest***: Unverified user that can check all items and profiles but can't put items on cart,create new items or make an order.

***Verified***: Verified user that can do everything that guest and more,
like put items on cart create new post's or make an order.

****Moderator****: Can do everything that guest and verified and little more, like update items and profiles of other users.

****Admin****: Can perform all functionalities provided by the API.
## Contacts
[Telegram](https://t.me/primeking666)

Discord: n1ghty_

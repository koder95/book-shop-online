# **Spring Boot Portfolio Project — Part 1.**

Getting a first job isn’t easy in any industry, but it’s *easier* if you have a portfolio of projects to demonstrate your skills. For a Java developer — Spring Boot should definitely be one of them. So, throughout the module, you’ll have the opportunity to create a simple (but worth showing!) online book store app. We’ll prepare it in fragments, moving from one homework to another.

## **First Homework**

Let’s start by creating a new GitHub repository, in which we’ll place a new Spring Boot project as well as the `pom.xml` with all required dependencies. Next, in no particular order:

* add a `Book` entity;  
* add a `BookRepository` entity;  
* add a `BookService` entity;
* configure database access;  
* configure checkstyle;  
* enter the `test/resources` folder to configure `application.properties`;  
* configure CI (Continuous Integration);  
* configure GitHub git branch flow.

## Git Flow

1. Create a GitHub repository and clone it to the local machine.   
2. Each homework assignment should be done in a separate branch. To do this, create a branch from the master branch and implement homework, then commit changes, push, and create a pull request. 

⚠️ Only after approving PR by mentors should you merge PR to master.   
**DO NOT** leave open pull requests after approval.   
**ONLY** one homework implementation should be provided in a pull request.

💡It is a good practice to provide an informative commit message to your pull request. It should be a short commit message of about 5 – 6 words with the main idea of homework that you implement

Regarding branches, **it’s good to remove one** when you merge a PR (except for the `main` and `master` branches, of course). And this action can be automated! Go to `Settings` in your GitHub repo, select `General`, scroll down, and choose the checkbox next to `Automatically delete head branches`.

![img_remove_branch](https://mate-academy-images.s3.eu-central-1.amazonaws.com/Znimok_ekrana_2023_09_07_o_14_39_44_c7dcc5ab36.png)

### **Tuning the Infrastructure**

Once the repository is up, we should tune it, beginning with the `Add checkstyle` plugin (for reference, see [this project's](https://github.com/mate-academy/hibernate-relations-hw) `pom.xml file`). Step by step:

1. In the project’s root directory, create a new file named `checkstyle.xml`.  
2. [Copy](https://raw.githubusercontent.com/mate-academy/style-guides/master/java/checkstyle.xml) to `checkstyle.xml`  
3. Add the following property to the `pom.xml` file’s `<properties>...</properties>` section:

     ```text  
      <maven.checkstyle.plugin.configLocation>checkstyle.xml</maven.checkstyle.plugin.configLocation>  
     ```

4. Add below plugin into the `<build> <plugins> .... </plugins> </build>` section:  
    
  ```xml  
  <plugin>  
      <groupId>org.apache.maven.plugins</groupId>  
      <artifactId>maven-checkstyle-plugin</artifactId>  
      <version>3.3.0</version>  
      <executions>  
          <execution>  
              <phase>compile</phase>  
              <goals>  
                  <goal>check</goal>  
              </goals>  
          </execution>  
      </executions>  
      <configuration>  
          <configLocation>${maven.checkstyle.plugin.configLocation}</configLocation>  
          <consoleOutput>true</consoleOutput>  
          <failsOnError>true</failsOnError>  
          <linkXRef>false</linkXRef>
      </configuration>
  </plugin>
  ```

Next, in your project’s root directory, create a `.github` directory  — it should start with dot! — then a `workflows` directory. You should end up with the root directory -> `.github` -> `workflows` schema ([here’s a reference]([https://github.com/mate-academy/hibernate-relations-hw](https://github.com/mate-academy/hibernate-relations-hw))). Inside the `workflows` directory, create a new file `ci.yml` and add this code:

```yaml  
name: Java CI

on:  
  - push  
  - pull_request

jobs:  
  build:  
    runs-on: ubuntu-latest

    steps:  
      - uses: actions/checkout@v4  
      - name: Set up JDK 17  
        uses: actions/setup-java@v4  
        with:  
          java-version: '17'  
          distribution: 'temurin'  
          cache: maven  
      - name: Build with Maven  
        run: mvn --batch-mode --update-snapshots verify  
```

Lastly, commit all above changes to GitHub. This time, we can commit infrastructure changes directly into the master (main) branch, but **all next changes should come in separate PRs.**

### **Creating The First Entity: Book**

This homework is a little lengthy, but we want your portfolio project to be truly worthy of showing\! And besides, it’s the last task for today. Let’s add to our project a book entity with fields:

- `id` (Long, PK);  
- `title` (String, not null);  
- `author` (String, not null);  
- `isbn` (String, not null, unique);  
- `price` (BigDecimal, not null);  
- `description` (String);  
- `coverImage` (String).

`BookRepository` interface with methods and the `BookRepositoryImpl` class:

- `Book save(Book book);`  
- `List<Book> findAll();`

`BookService` interface with methods and the `BookServiceImpl` class:

- `Book save(Book book);`   
- `List<Book> findAll();`

…and a class denoted as `@SpringBootApplication` that has a `CommandLineRunner` bean where you should test all `BookService` methods. Don’t forget to add MySQL connection properties to the `main/application.properties` file:

```text  
spring.jpa.hibernate.ddl-auto=create-drop  
spring.jpa.show-sql=true  
```

### **What if CI checks don’t pass?**

Once you add MySQL to the project, CI checks on GitHub might fail — to prevent this:

1. Create a new `resources` folder in the `src/test` directory.  
2. Create a new `application.properties` folder in the `src/test/resources` folder.  
3. Add below code to the `application.properties` file:

   ```  
   spring.datasource.url=jdbc:h2:mem:testdb  
   spring.datasource.driverClassName=org.h2.Driver  
   spring.datasource.username=sa  
   spring.datasource.password=password  
   spring.jpa.database-platform=org.hibernate.dialect.H2Dialect  
   ```

4. Add this dependency to the `pom.xml` file:

```xml
        <dependency>  
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
            <scope>test</scope>
        </dependency>
```

Now, our builds should pass!

## **Project Overview**

The project’s aim is to deliver a fully-functioning app for an online book store. We will create eight domain models (a.k.a. entities):

- **User,** containing data about the registered user, such as their authentication details and personal information;  
- **Role,** representing the user’s role in the system (admin, user).  
- **Book,** representing a book available in the store.  
- **Category,** representing a category books can belong to.  
- **ShoppingCart,** representing a user's shopping cart.  
- **CartItem,** representing an item in the user's shopping cart.  
- **Order,** representing an order placed by a user.  
- **OrderItem,** representing an item in a user's order.

Role division in the app:

**1. Shopper (user), who can…**

  - sign up and sign in to the store;  
  - browse all book categories at once or just a selected one;  
  - view each book in detail;  
  - put books in and out of their cart;  
  - view the cart;  
  - buy all the books in the basket;  
  - view past orders;  
  - view all books in a single order;  
  - view each book from one order in detail.

**2. Manager (admin), who can…**

  - add and remove books from the store;  
  - update book details;  
  - create and delete book categories;  
  - update category details;  
  - update order status to, say, "shipped" or "delivered".  

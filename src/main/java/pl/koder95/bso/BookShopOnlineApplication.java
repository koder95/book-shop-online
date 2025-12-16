package pl.koder95.bso;

import java.math.BigDecimal;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import pl.koder95.bso.service.BookService;
import pl.koder95.bso.model.Book;

@SpringBootApplication
public class BookShopOnlineApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookShopOnlineApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Book book1 = new Book();
            book1.setAuthor("Stefan Żeromski");
            book1.setTitle("Przedwiośnie");
            book1.setIsbn("978-83-7327-195-1");
            book1.setPrice(BigDecimal.valueOf(19.95));
            BookService bookService = ctx.getBean(BookService.class);
            bookService.save(book1);
        };
    }
}

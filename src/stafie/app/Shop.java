package stafie.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.function.Predicate;

import stafie.data.Drink;
import stafie.data.Food;
import stafie.data.Product;
import stafie.data.ProductManager;
import stafie.data.Rateable;
import stafie.data.Rating;

public class Shop {

	public static void main(String[] args) {
        
		ProductManager pm = new ProductManager(Locale.UK);

		pm.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
    	pm.printProductReport(101);
//		pm.reviewProduct(101, Rating.FOUR_STAR, "Nice hot cup of Tea");
		pm.parseReview("101,4,Nice hot cup of tea");
//		pm.reviewProduct(101, Rating.TWO_STAR, "Rather weak Tea");
//		pm.reviewProduct(101, Rating.FOUR_STAR, "Fine Tea");
//		pm.reviewProduct(101, Rating.FOUR_STAR, "Good Tea");
//		pm.reviewProduct(101, Rating.FIVE_STAR, "Perfect Tea");
//		pm.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");

		pm.printProductReport(101);
		pm.createProduct(102, "Coffee", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
		pm.reviewProduct(102, Rating.THREE_STAR, "Coffee was ok");
		pm.reviewProduct(102, Rating.ONE_STAR, "Where is the milk?");
		pm.reviewProduct(102, Rating.FIVE_STAR, "It s perfect with ten spoons of sugar");

		pm.createProduct(103, "Cake", BigDecimal.valueOf(3.99), Rating.NOT_RATED, LocalDate.now().plusDays(2));
		pm.reviewProduct(103, Rating.FIVE_STAR, "Very nice Cake");
		pm.reviewProduct(103, Rating.FOUR_STAR, "It's good, but I ve expected more chocolate");
		pm.reviewProduct(103, Rating.FIVE_STAR, "It s perfect with ten spoons of sugar");

		pm.createProduct(104, "Cookie", BigDecimal.valueOf(2.99), Rating.NOT_RATED, LocalDate.now());
		pm.reviewProduct(104, Rating.THREE_STAR, "Just another cookie");
		pm.reviewProduct(104, Rating.THREE_STAR, "Ok");

		pm.createProduct(105, "Hot Chocolate", BigDecimal.valueOf(2.50), Rating.NOT_RATED);
		pm.reviewProduct(105, Rating.FOUR_STAR, "Tasty!");
		pm.reviewProduct(105, Rating.FOUR_STAR, "Not bad at all");

		pm.createProduct(106, "Chocolate", BigDecimal.valueOf(2.50), Rating.NOT_RATED,LocalDate.now().plusDays(3));
		pm.reviewProduct(106, Rating.TWO_STAR, "Too sweet");
		pm.reviewProduct(106, Rating.THREE_STAR, "Better than cookie");
		pm.reviewProduct(106, Rating.TWO_STAR, "Too bitter");
		pm.reviewProduct(106, Rating.ONE_STAR, "I don t get it!");
//		pm.printProductReport(106);
		
		
		pm.getDiscounts().forEach((rating,discount)->System.out.println(rating+'\t'+discount));
		
		Predicate<Product> filter= p->p.getPrice().floatValue()<2;
		Comparator<Product> ratingSorter =(p1,p2)->p2.getRating().ordinal()-p1.getRating().ordinal();
		Comparator<Product> priceSorter = (p1,p2)->p1.getPrice().compareTo(p2.getPrice());
		Comparator<Product> priceRating = ratingSorter.thenComparing(priceSorter);
    	pm.printProducts(ratingSorter,filter);
//		pm.printProducts(priceSorter);
//		pm.printProducts(priceRating);
    	
    	
    	
    	
    	
    	
	}
}

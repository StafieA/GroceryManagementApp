package stafie.data;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.OptionalDouble;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {
  
	
	
	private ResourceBundle config = ResourceBundle.getBundle("stafie.data.config");
    private MessageFormat reviewFormat = new MessageFormat(config.getString("review.data.format"));
    private MessageFormat productFormat = new MessageFormat(config.getString("product.data.format"));
    private static final Logger logger =Logger.getLogger(ProductManager.class.getName());
    private Path reportsFolder =Path.of(config.getString("reports.folder"));
    private Path dataFolder =Path.of(config.getString("data.folder"));
    private Path tempFolder =Path.of(config.getString("temp.folder"));
//	private Product product;
//	private Review[] reviews = new Review[5];
	private  ResourceFormatter formatter;
    private Map<Product,List<Review>> products = new HashMap<>();
    private static Map<String, ResourceFormatter> formatters= Map.of(
    		    "en-GB",new ResourceFormatter(Locale.UK),
    		    "en-US",new ResourceFormatter(Locale.US),
    		    "fr-FR",new ResourceFormatter(Locale.FRANCE),
    		    "ru-RU",new ResourceFormatter(new Locale("ru","RU")),
    		    "zh-CN",new ResourceFormatter(Locale.CHINA),
    		    "ro-RO",new ResourceFormatter(new Locale("ro","RO")),
    		    "de-DE",new ResourceFormatter(Locale.GERMANY),
    		    "it-IT",new ResourceFormatter(Locale.ITALY));
    
    private static class ResourceFormatter{
    	
    	private ResourceFormatter(Locale locale) {
    		this.locale = locale;
    		resources = ResourceBundle.getBundle("stafie.data.resources", locale);
    		dateFormat = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).localizedBy(locale);
    		moneyFormat = NumberFormat.getCurrencyInstance(locale);
    	}
    	private Locale locale ;
        private ResourceBundle resources;
        private DateTimeFormatter dateFormat;
        private NumberFormat moneyFormat;
        
        private String formatProduct(Product product) {
        	return MessageFormat.format(resources.getString("product"),
				    product.getName(),
				    moneyFormat.format(product.getPrice()),
				    product.getRating().getStars(),
				    dateFormat.format(product.getBestBefore()));
        }
        private String formatReview(Review review) {
        	return MessageFormat.format(resources.getString("review"),
					review.getRating().getStars(),
					review.getComments());
        }
        
        private String getText(String key) {
        	return resources.getString(key);
        }
    	
    }
	
//	public ProductManager() {};
	
	public ProductManager(Locale locale) {
		this(locale.toLanguageTag());
//		formatter = new ResourceFormatter(locale);
	}
	public ProductManager(String languageTag) {
		changeLocale(languageTag);
	}
	public void changeLocale(String languageTag) {
		formatter = formatters.getOrDefault(languageTag, formatters.get("en-GB"));
	}

	public Product createProduct(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		Product product = new Food(id, name, price,rating, bestBefore);
		products.putIfAbsent(product, new ArrayList<>());
		return product;
	}
	
	public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
		Product product = new Drink(id, name, price,rating);
		products.putIfAbsent(product, new ArrayList<>());
		return product;
	}
	
	public Product findProduct(int id) throws ProductManagerException {
		
	return	products.keySet()
		        .stream()
		        .filter(p->p.getId()==id)
		        .findFirst()
		        .orElseThrow(()-> new ProductManagerException("Product with id"+id+"not found"));
//		Product result=null;
//		
//		for(Product product :products.keySet()) {
//			if(product.getId()==id) {
//				result=product;
//				break;
//			}
//		}
//		return result;
	}
	
	public Product reviewProduct(int id, Rating rating, String comments)  {
		try {
			return reviewProduct(findProduct(id),rating,comments);
		} catch (ProductManagerException e) {
			logger.log(Level.INFO, e.getMessage());
			
			
		}
		return null;
	}
	
	public Product reviewProduct (Product product, Rating rating, String comments) {

		List<Review> reviews = products.get(product);
		   products.remove(product, reviews);
		   reviews.add(new Review(rating, comments));
OptionalDouble average=reviews
                  .stream()
		          .mapToInt(p->p.getRating()
		          .ordinal())
		          .average();
        double averageFinal=average.orElse(0.0);
//		          .orElse(0);
//	         int sum=0;
//	         for(Review review:reviews) {
//	        	 sum+=review.getRating().ordinal();
//	         }
//	    
	         product = product.applyRating(Rateable.convert((int)Math.round(averageFinal)));
	         products.put(product, reviews);
	         return product;
	}
	
	public void printProductReport(int id)  {
		try {
			printProductReport(findProduct(id));
		} catch (ProductManagerException e) {
			logger.log(Level.INFO, e.getMessage());
			
		}
	}
	
	public void printProductReport(Product product) {
		StringBuilder txt = new StringBuilder();
		txt.append(formatter.formatProduct(product));
		
		txt.append('\n');
		List<Review> reviews = products.get(product);
		Collections.sort(reviews);
		if(reviews.isEmpty()) {
		txt.append(formatter.getText("no.reviews")+'\n');
	      }else {
	    	  txt.append(reviews.stream().map(r->formatter.formatReview(r)+'\n')
	    			                     .collect(Collectors.joining()));
	      }
		       
          System.out.println(txt);
	}
	
	public void printProducts(Comparator<Product>sorter,Predicate<Product> filter) {
//		List<Product> productList = new ArrayList<>(products.keySet());
//		productList.sort(sorter);
		StringBuilder txt = new StringBuilder();
		products.keySet()
		        .stream()
		        .sorted(sorter)
		        .filter(filter)
		        .forEach(p->txt.append(formatter.formatProduct(p)+'\n'));
//		for(Product product : productList) {
//			txt.append(formatter.formatProduct(product));
//			txt.append('\n');
//		}
		System.out.println(txt);
	}
	 public Map<String, String> getDiscounts(){
		return products
  .keySet()
  .stream()
  .collect(Collectors.groupingBy(product->product.getRating().getStars(),
		                		  Collectors.collectingAndThen(Collectors.summingDouble(p->p.getDiscount().doubleValue()), 
		                				                                                discount->formatter.moneyFormat.format(discount))));
	 }
	 public void parseReview(String text) {
		 try {
			Object[]values = reviewFormat.parse(text);
			reviewProduct(Integer.parseInt((String)values[0]),
					      Rateable.convert(Integer.parseInt((String)values[1])),
					      (String)values[2]);
		} catch (ParseException | NumberFormatException e) {
			logger.log(Level.WARNING,"Error parsing review "+text,e);
			
		}
	 }
	 public void parseProduct(String text) {
		 try {
			Object[]values = reviewFormat.parse(text);
			
			int id = Integer.parseInt((String)values[1]);
			String name = (String)values[2];
			BigDecimal price=BigDecimal.valueOf(Double.parseDouble((String)values[3]));
			Rating rating = Rateable.convert(Integer.parseInt((String)values[4]));
			switch((String)values[0]) {
			case "D":
				createProduct(id,name,price,rating);
				break;
			case "F":
				LocalDate bestBefore = LocalDate.parse((String)values[5]);
				createProduct(id,name,price,rating,bestBefore);
			}
			
			
		} catch (ParseException | NumberFormatException|DateTimeParseException e) {
			logger.log(Level.WARNING,"Error parsing product "+text,e);
			
		}
	 }
}

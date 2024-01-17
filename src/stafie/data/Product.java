package stafie.data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

import static java.math.RoundingMode.*;
import static stafie.data.Rating.*;

public abstract  class Product implements Rateable<Product>{
  
	public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
	
	private int id;
	private String name;
	private BigDecimal price;
	private Rating rating;
	
	 Product() {
		this(0,"No_Name",BigDecimal.ZERO);
	}
	
	
	 Product(int id, String name, BigDecimal price, Rating rating) {
		
		this.id = id;
		this.name = name;
		this.price = price;
		this.rating = rating;
	}
	 Product(int id, String name, BigDecimal price) {
		this(id,name,price,NOT_RATED);
		}
	public LocalDate getBestBefore() {
		return LocalDate.now();
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
//	public void setId(final int id) {
//		this.id = id;
//	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
//	public void setName(final String name) {
//		this.name = name;
//	}
	/**
	 * @return the price
	 */
	public BigDecimal getPrice() {
		return price;
	}
	/**
	 * @param price the price to set
	 */
//	public void setPrice(final BigDecimal price) {
////		price =BigDecimal.ZERO;
//		this.price = price;
//	}
	public BigDecimal getDiscount() {
		return price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
	}
	
	public Rating getRating() {
		return rating;
	}
	public abstract Product applyRating( Rating newRating) ;

	@Override
	public String toString() {
		return "Product  " + id + " " + name + ", price " + price + ", Discount "
	            +getDiscount()+", Rating " + rating.getStars()+"  "+getBestBefore() ;
	}


	@Override
	public int hashCode() {
		return Objects.hash(id);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj )
			return true;
//		if (obj == null || getClass() != obj.getClass())
//			return false;
		if(obj instanceof Product) {
		
		Product other = (Product) obj;
		return id == other.id 
//				&& name.equals(other.name) 
//				&& 
////				Objects.equals(price, other.price)&&
////				rating == other.rating
				;}
		return false;
	}
	
	
}

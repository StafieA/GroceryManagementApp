package stafie.data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author 40751
 *
 */
public class Food extends Product {
    
	private LocalDate bestBefore;
	
	

	 Food(int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
		super(id, name, price, rating);
		this.bestBefore = bestBefore;
	}

	/**
	 * @return the bestBefore
	 */
	public LocalDate getBestBefore() {
		return bestBefore;
	}
	
	

	@Override
	public BigDecimal getDiscount() {
		return bestBefore.equals(LocalDate.now()) ? super.getDiscount():BigDecimal.ZERO;
	}

	@Override
	public String toString() {
		return super.toString()+", BestBefore " + bestBefore;
	}

	@Override
	public Product applyRating(Rating newRating) {
		return new Food(getId(), getName(), getPrice(), newRating, bestBefore);
	}

}

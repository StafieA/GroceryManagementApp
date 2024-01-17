package stafie.data;

@FunctionalInterface
public interface Rateable<T> {
   
	public static final Rating DEFAULT_RATING=Rating.NOT_RATED;
	
	T applyRating (Rating rating);
	
	default T applyRating(int stars) {
		return applyRating(convert(stars));
	}
	
	default Rating getRating() {
		return DEFAULT_RATING;
	}
	
	static Rating convert(int i) {
		 return (i>=0 && i<=5) ? Rating.values()[i] : DEFAULT_RATING;
	}
	
	
	
}

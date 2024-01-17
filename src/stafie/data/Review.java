package stafie.data;

public class Review implements Comparable<Review>{
    
	private Rating rating;
	private String comments;
	
	public Review(Rating rating, String comments) {
		
		this.rating = rating;
		this.comments = comments;
	}

	/**
	 * @return the rating
	 */
	public Rating getRating() {
		return rating;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	@Override
	public String toString() {
		return "Review [rating=" + rating + ", comments=" + comments + "]";
	}

	@Override
	public int compareTo(Review o) {
		
		return o.getRating().ordinal()-this.getRating().ordinal();
	}
	
	
	
	
}

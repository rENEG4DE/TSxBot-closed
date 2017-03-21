package utility.bulletin;

public interface AbstractBulletin {
	public void push (String bullet, String[] posDesc, Object[] values);
	public void push (String bullet, String[] posDesc);
}
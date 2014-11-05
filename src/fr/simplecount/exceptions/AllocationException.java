package fr.simplecount.exceptions;

public class AllocationException extends Exception {
	private static final long serialVersionUID = 1L;
	public AllocationException() { super(); }
	public AllocationException(String message) { super(message); }
	public AllocationException(String message, Throwable cause) { super(message, cause); }
	public AllocationException(Throwable cause) { super(cause); }
}

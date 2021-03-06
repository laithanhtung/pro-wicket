package com.apress.wicketbook.extensions;

// Other imports
import wicket.Application;
import wicket.Component;
import wicket.model.AbstractReadOnlyDetachableModel;
import wicket.model.IModel;

import com.apress.wicketbook.common.Book;


public class DetachableBookModel extends AbstractReadOnlyDetachableModel {
	// Required minimal information to look up the book later
	private final int id;

	// Adds "transient" modifier to prevent serialization
	private transient Book book;

	public DetachableBookModel(Book book) {
		this(book.getId());
		this.book = book;
	}

	public DetachableBookModel(int id) {
		if (id == 0) {
			throw new IllegalArgumentException();
		}
		this.id = id;
	}

	/**
	 * Returns null to indicate there is no nested model
	 */
	@Override
	public IModel getNestedModel() {
		return null;
	}

	/**
	 * Uses the DAO to load the required Book object when the model is attached
	 * to the request
	 */
	@Override
	protected void onAttach() {
		book = getBookDao().getBook(id);
	}

	/**
	 * Clear the reference to the contact when the model is detached.
	 */
	@Override
	protected void onDetach() {
		book = null;
	}

	/**
	 * Called after onAttach to return the detachable object.
	 * 
	 * @param component
	 *            The component asking for the object
	 * @return The detachable object.
	 */
	@Override
	protected Object onGetObject(Component component) {
		return book;
	}

	private BookDao getBookDao() {
		return ((BookStoreApplication) Application.get()).getBookDao();
	}
}

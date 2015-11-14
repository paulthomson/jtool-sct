package org.objectweb.dsrg.cocome.fractal.tradingsystem.inventory;

/**
 * <code>ProductTO</code> is used as transfer object class for transferring basic product information
 * between client and the service-oriented application layer. It contains either copies of persisted
 * data which are transferred to the client, or data which is transferred from the client to the
 * application layer for being processed and persisted.
 *
 */
public class ProductTO {

	protected long id;
	protected long barcode;
	protected double purchasePrice;
	protected String name;

	/**
	 * Gets id.
	 * @return The identifier.
	 */
	public long getId() {
		return id;
	}
	
	/**
	 * sets the id.
	 *
	 */
	public void setId(final long id) {
		this.id = id;
	}

	/**
	 * Gets barcode value.
	 * @return Saved barcode value.
	 */
	public long getBarcode() {
		return barcode;
	}

	/**
	 * Sets barcode value.
	 * @param barcode
	 */
	public void setBarcode(final long barcode) {
		this.barcode = barcode;
	}

	/**
	 * Gets name of the product.
	 * @return Name of product.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets name of product.
	 * @param name New name.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Gets purchase price of product.
	 * @return Saved purchase price.
	 */
	public double getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * Sets purchase price of product.
	 * @param purchasePrice Purchase price to e set.
	 */
	public void setPurchasePrice(final double purchasePrice) {
		this.purchasePrice = purchasePrice;
	}


	/**
	 * Checks public attributes for equality
	 * <p>
	 * required for UC 8 (class AmplStarter)
	 */
	@Override
	public boolean equals(final Object obj) {
		if(obj == null || !obj.getClass().equals(this.getClass())) {
			System.out.println("KK: objects not equal");
			return false;
		} 
		
		final ProductTO pTO = (ProductTO) obj;
		if(this.getBarcode() == pTO.getBarcode() &&
				this.getId() == pTO.getId() &&
				this.getName().equals(pTO.getName()) &&
				this.getPurchasePrice() == pTO.getPurchasePrice() ) {

			return true;			
		}
		
		return false;
	}
}

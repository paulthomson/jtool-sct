package org.objectweb.dsrg.cocome.fractal.tradingsystem;

import org.objectweb.dsrg.cocome.fractal.tradingsystem.inventory.ProductTO;


/**
 * <code>ProductWithStockItemTO</code> is used as transfer object class for transferring basic product information and additional stock item information.
 * It references a single stock item for a specific store.
 * between client and the service-oriented application layer. It contains either copies of persisted
 * data which are transferred to the client, or data which is transferred from the client to the
 * application layer for being processed and persisted.
 *
 */
public class ProductWithStockItemTO extends ProductTO {

	protected StockItemTO stockItemTO;

	/**
	 * Gets the saved stock item transfer object.
	 * @return The referred stock item transfer object.
	 */
	public StockItemTO getStockItemTO() {
		return stockItemTO;
	}

	/**
	 * Sets stock item transfer object.
	 * @param stockItemTO New stock item transfer object.
	 */
	public void setStockItemTO(final StockItemTO stockItemTO) {
		this.stockItemTO = stockItemTO;
	}
}

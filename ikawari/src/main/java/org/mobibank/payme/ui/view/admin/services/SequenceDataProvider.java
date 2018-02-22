package org.mobibank.payme.ui.view.admin.services;

import java.util.ArrayList;
import java.util.List;

import org.mobibank.payme.BeanLocator;
import org.mobibank.payme.backend.data.entity.Sequences;
import org.mobibank.payme.backend.services.SequenceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;
import org.vaadin.spring.annotation.PrototypeScope;

import com.vaadin.data.provider.Query;
import com.vaadin.data.provider.QuerySortOrder;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@PrototypeScope
public class SequenceDataProvider extends FilterablePageableDataProvider<Sequences, Object>{

	/**
	 * 
	 */
	private transient SequenceService sequenceService;

	protected SequenceService getUserService() {
		if (sequenceService == null) {
			sequenceService = BeanLocator.find(SequenceService.class);
		}
		return sequenceService;
	}

	@Override
	protected Page<Sequences> fetchFromBackEnd(Query<Sequences, Object> arg0, Pageable pageable) {
		return getUserService().findAnyMatching(getOptionalFilter(), pageable);
	}

	@Override
	protected List<QuerySortOrder> getDefaultSortOrders() {
		List<QuerySortOrder> sortOrders = new ArrayList<>();
		sortOrders.add(new QuerySortOrder("sequence", SortDirection.ASCENDING));
		return sortOrders;
	}

	@Override
	protected int sizeInBackEnd(Query<Sequences, Object> query) {
		return (int) getUserService().countAnyMatching(getOptionalFilter());
	}

}

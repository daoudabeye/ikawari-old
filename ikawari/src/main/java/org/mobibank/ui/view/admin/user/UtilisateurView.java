package org.mobibank.ui.view.admin.user;

import org.mobibank.backend.data.entity.Utilisateur;
import org.mobibank.ui.AbstractCrudView;
import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.BeanValidationBinder;
import com.vaadin.data.ValidationResult;
import com.vaadin.data.Validator;
import com.vaadin.data.ValueContext;
import com.vaadin.data.validator.BeanValidator;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Component.Focusable;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TextField;

@SpringView
public class UtilisateurView extends AbstractCrudView<Utilisateur> {

	/**
	 * 
	 */
	private final UtilisateurPresenter presenter;
	private final UtilisateurViewDesign viewDesign;

	private boolean passwordRequired;

	/**
	 * Custom validator to be able to decide dynamically whether the password
	 * field is required or not (empty value when updating the user is
	 * interpreted as 'do not change the password').
	 */
	private Validator<String> passwordValidator = new Validator<String>() {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		BeanValidator passwordBeanValidator = new BeanValidator(Utilisateur.class, "password");

		@Override
		public ValidationResult apply(String value, ValueContext context) {
			if (!passwordRequired && value.isEmpty()) {
				// No password required and field is empty
				// OK regardless of other restrictions as the empty value will
				// not be used
				return ValidationResult.ok();
			} else {
				return passwordBeanValidator.apply(value, context);
			}
		}
	};

	@Autowired
	public UtilisateurView(UtilisateurPresenter presenter) {
		this.presenter = presenter;
		viewDesign = new UtilisateurViewDesign();
	}

	@Override
	public void init() {
		super.init();
		presenter.init(this);
	}

	@Override
	public UtilisateurViewDesign getViewComponent() {
		return viewDesign;
	}

	@Override
	protected UtilisateurPresenter getPresenter() {
		return presenter;
	}

	@Override
	protected Grid<Utilisateur> getGrid() {
		return getViewComponent().list;
	}

	@Override
	protected void setGrid(Grid<Utilisateur> grid) {
		getViewComponent().list = grid;
	}

	@Override
	protected Component getForm() {
		return getViewComponent().form;
	}

	@Override
	protected Button getAdd() {
		return getViewComponent().add;
	}

	@Override
	protected Button getCancel() {
		return getViewComponent().cancel;
	}

	@Override
	protected Button getDelete() {
		return getViewComponent().delete;
	}

	@Override
	protected Button getUpdate() {
		return getViewComponent().update;
	}

	@Override
	protected TextField getSearch() {
		return getViewComponent().search;
	}

	@Override
	protected Focusable getFirstFormField() {
		return getViewComponent().username;
	}

	@Override
	public void bindFormFields(BeanValidationBinder<Utilisateur> binder) {
		getViewComponent().bindForm(binder);
		binder.forField(getViewComponent().password).withValidator(passwordValidator).bind(bean -> "",
				(bean, value) -> {
					if (value.isEmpty()) {
						// If nothing is entered in the password field, do
						// nothing
					} else {
						bean.setPassword(presenter.encodePassword(value));
					}
				});
		binder.bindInstanceFields(getViewComponent());
	}
	
	public void setPasswordRequired(boolean passwordRequired) {
		this.passwordRequired = passwordRequired;
		getViewComponent().password.setRequiredIndicatorVisible(passwordRequired);
	}
}

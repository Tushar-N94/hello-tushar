package com.vivekfin.model.security;

import java.io.IOException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vivekfin.dto.NavigationReturnEntity;
import com.vivekfin.dto.OutletReturnEntity;
import com.vivekfin.model.common.Views.Public;
import com.vivekfin.model.common.Views.Seller;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 */
@NoArgsConstructor
@Getter
@Setter
@Entity
@JsonInclude(Include.NON_EMPTY)
public class Navigation {

	@JsonIgnore
	@Transient
	private Logger log = LoggerFactory.getLogger(Navigation.class);
	
	
	@Id
	@SequenceGenerator(name = "navigationGen", sequenceName = "navigation_generator")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "navigationGen")
	@Column(name = "id")
	@JsonView(Seller.class)
	private Long id;


	@Column(name = "buyflow_json",length = 2000)
	private String buyFlowJson;


	@JsonView({ Public.class, Seller.class })
	@Transient
	private NavigationReturnEntity navigationReturnEntity;
	
	public NavigationReturnEntity getNavigationReturnEntity() {
		NavigationReturnEntity navigationReturn = this.navigationReturnEntity;
		ObjectMapper mapper = new ObjectMapper();

		if (navigationReturn == null) {
			try {
				if (this.buyFlowJson != null) {
					navigationReturnEntity = mapper.readValue(this.buyFlowJson, NavigationReturnEntity.class);
				} else {
					navigationReturnEntity = new NavigationReturnEntity();
				}
			} catch (IOException e) {
				log.warn("Unable to convert json {} to BasketEntity object", buyFlowJson, e);
				navigationReturnEntity = null;
			}
		}
		
		return navigationReturnEntity;
		
	}
	
	public void setNavigationReturn(NavigationReturnEntity navigationReturn) {
		this.navigationReturnEntity = navigationReturn;
	}
	
}

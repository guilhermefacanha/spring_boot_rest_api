package lab.microservice.labservice.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lab.microservice.labservice.serializer.JsonCurrencySerializer;
import lab.microservice.labservice.serializer.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
@Data
@Entity
@Table()
public class User extends EntityBase{
	private static final long serialVersionUID = 8542522994899388913L;
	
	@Column(nullable=false)
	private String name;
	@Column(nullable=false)
	private String email;
	private String phone;
	@Column(nullable=false)
	private String city;
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date birthDate;
	
	@JsonSerialize(using = JsonCurrencySerializer.class)
	private double salary;
}

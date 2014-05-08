package com.elitethought.entity;

import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("serial")
@Entity
@Table(name = "account")
@NamedQuery(name = Account.FIND_BY_EMAIL, query = "select a from Account a where a.email = :email")
public class Account implements java.io.Serializable {

	public static final String FIND_BY_EMAIL = "Account.findByEmail";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "accountId")
	private Long id;

	@Column(unique = true, nullable = false)
	private String email;
	
	@JsonIgnore
    @Column(nullable = false)
	private String password;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name="role_account",
               joinColumns = @JoinColumn(name="roleId"),
               inverseJoinColumns = @JoinColumn(name="accountId"))
	private Set<Role> roles = new HashSet<>();

    protected Account() {

	}
	
	public Account(String email, String password, Role role) {
		this.email = email;
		this.password = password;
        this.roles.add(role);
	}

    public Account(String email, String password, Set<Role> roles) {
        this.email=email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
		return id;
	}

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
}

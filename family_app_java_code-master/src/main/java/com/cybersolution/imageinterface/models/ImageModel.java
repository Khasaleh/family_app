package com.cybersolution.imageinterface.models;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

import javax.persistence.GeneratedValue;

import javax.persistence.GenerationType;

import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;

import lombok.Data;

import lombok.Getter;

import lombok.NoArgsConstructor;

import lombok.Setter;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "image_model")
public class ImageModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Date date;
	private String extension;

	@JsonManagedReference
	@ManyToOne
	@JoinColumn(name="user_id",referencedColumnName="id")
	private User user;

	@Column(columnDefinition = "TEXT")
	private String localPath;
	
}
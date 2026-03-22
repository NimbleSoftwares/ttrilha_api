package com.nimblesoftwares.ttrilha_api.domain.user.model;

import java.time.OffsetDateTime;
import java.util.UUID;

public class User {

  private UUID id;
  private OffsetDateTime deletedAt;
  private OffsetDateTime updatedAt;
  private OffsetDateTime createdAt;
  private String avatarUrl;
  private String lastName;
  private String firstName;
  private String displayName;
  private String email;
  private String username;
  private String city;
  private String state;

  public User(){}

  public User(String email, String displayName, String firstName, String lastName, String avatarUrl) {
    this.email = email;
    this.displayName = displayName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
  }

  public User(String email, String displayName, String firstName, String lastName, String avatarUrl, String username) {
    this.email = email;
    this.displayName = displayName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
    this.username = username;
  }

  public User(UUID id, String email, String displayName, String firstName, String lastName, String avatarUrl, OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
    this.id = id;
    this.email = email;
    this.displayName = displayName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public User(UUID id, String email, String displayName, String firstName, String lastName, String avatarUrl, String username, OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt) {
    this.id = id;
    this.email = email;
    this.displayName = displayName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
    this.username = username;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
  }

  public User(UUID id, String email, String displayName, String firstName, String lastName, String avatarUrl, String username, OffsetDateTime createdAt, OffsetDateTime updatedAt, OffsetDateTime deletedAt, String city, String state) {
    this.id = id;
    this.email = email;
    this.displayName = displayName;
    this.firstName = firstName;
    this.lastName = lastName;
    this.avatarUrl = avatarUrl;
    this.username = username;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.deletedAt = deletedAt;
    this.city = city;
    this.state = state;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public OffsetDateTime getDeletedAt() {
    return deletedAt;
  }

  public void setDeletedAt(OffsetDateTime deletedAt) {
    this.deletedAt = deletedAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getCity() {
    return city;
  }

  public void setCity(String city) {
    this.city = city;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

}

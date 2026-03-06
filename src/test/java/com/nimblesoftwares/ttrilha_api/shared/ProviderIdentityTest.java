package com.nimblesoftwares.ttrilha_api.shared;

import com.nimblesoftwares.ttrilha_api.adapter.in.web.shared.ProviderIdentity;
import com.nimblesoftwares.ttrilha_api.domain.user.exception.InvalidProviderException;
import com.nimblesoftwares.ttrilha_api.domain.user.model.ProviderEnum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("ProviderIdentity Test")
class ProviderIdentityTest {

  @Test
  @DisplayName("happy path - parses valid Google sub correctly")
  void test_shouldParseGoogleSubCorrectly() {
    // Arrange & Act
    ProviderIdentity identity = ProviderIdentity.fromSub("google-oauth2|1234567890");

    // Assert
    assertThat(identity.provider()).isEqualTo(ProviderEnum.GOOGLE);
    assertThat(identity.providerUserId()).isEqualTo("1234567890");
  }

  @Test
  @DisplayName("happy path - parses valid Apple sub correctly")
  void test_shouldParseAppleSubCorrectly() {
    ProviderIdentity identity = ProviderIdentity.fromSub("apple|user_id_xyz");

    assertThat(identity.provider()).isEqualTo(ProviderEnum.APPLE);
    assertThat(identity.providerUserId()).isEqualTo("user_id_xyz");
  }

  @Test
  @DisplayName("edge case - throws when separator is missing")
  void test_shouldThrowWhenSeparatorIsMissing() {
    assertThatThrownBy(() -> ProviderIdentity.fromSub("google-oauth2user123"))
        .isInstanceOf(InvalidProviderException.class)
        .hasMessageContaining("Invalid sub format");
  }

  @Test
  @DisplayName("edge case - throws when user ID is empty after separator")
  void test_shouldThrowWhenUserIdIsEmptyAfterSeparator() {
    assertThatThrownBy(() -> ProviderIdentity.fromSub("google-oauth2|"))
        .isInstanceOf(InvalidProviderException.class)
        .hasMessageContaining("Invalid sub format");
  }

  @Test
  @DisplayName("edge case - throws when provider is unknown")
  void test_shouldThrowWhenProviderIsUnknown() {
    assertThatThrownBy(() -> ProviderIdentity.fromSub("facebook|123"))
        .isInstanceOf(InvalidProviderException.class);
  }

  @Test
  @DisplayName("edge case - throws when sub is null")
  void fromSub_nullSub_throwsNullPointer() {
    assertThatThrownBy(() -> ProviderIdentity.fromSub(null))
        .isInstanceOf(InvalidProviderException.class);
  }
}
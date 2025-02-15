/**
 * @Date : 2025. 02. 15.
 * @author : jieun(je-pa)
 */
package com.toyland.category.model.entity;

import com.toyland.category.presentation.dto.CreateCategoryRequestDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "p_category")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "category_id")
  private UUID id;

  @Column(name = "category_name", nullable = false, length = 30)
  private String name;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_category_id")
  private Category parent;

  @Builder
  private Category(String name, Category parent) {
    this.name = name;
    this.parent = parent;
  }

  public static Category from(CreateCategoryRequestDto dto, Category parent) {
    return Category.builder()
        .name(dto.name())
        .parent(parent)
        .build();
  }
}

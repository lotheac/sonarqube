/*
 * Sonar, open source software quality management tool.
 * Copyright (C) 2008-2012 SonarSource
 * mailto:contact AT sonarsource DOT com
 *
 * Sonar is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * Sonar is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Sonar; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02
 */
package org.sonar.api.utils;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.matchers.JUnitMatchers.hasItem;

public class FieldUtilsTest {

  @Test
  public void shouldGetFieldsOfSingleClass() {
    List<Field> fields = FieldUtils.getFields(FieldsWithDifferentModifiers.class, true);
    assertThat(fields, hasItem(new FieldMatcher("publicField")));
    assertThat(fields, hasItem(new FieldMatcher("protectedField")));
    assertThat(fields, hasItem(new FieldMatcher("packageField")));
    assertThat(fields, hasItem(new FieldMatcher("privateField")));
    assertThat(fields, hasItem(new FieldMatcher("publicStaticField")));
    assertThat(fields, hasItem(new FieldMatcher("protectedStaticField")));
    assertThat(fields, hasItem(new FieldMatcher("packageStaticField")));
    assertThat(fields, hasItem(new FieldMatcher("privateStaticField")));
    assertThat(fields.size(), is(8));
  }

  @Test
  public void shouldGetFieldsOfClassHierarchy() {
    List<Field> fields = FieldUtils.getFields(Child.class, true);

    assertThat(fields, hasItem(new FieldMatcher("publicField")));
    assertThat(fields, hasItem(new FieldMatcher("protectedField")));
    assertThat(fields, hasItem(new FieldMatcher("packageField")));
    assertThat(fields, hasItem(new FieldMatcher("privateField")));
    assertThat(fields, hasItem(new FieldMatcher("publicStaticField")));
    assertThat(fields, hasItem(new FieldMatcher("protectedStaticField")));
    assertThat(fields, hasItem(new FieldMatcher("packageStaticField")));
    assertThat(fields, hasItem(new FieldMatcher("privateStaticField")));

    assertThat(fields, hasItem(new FieldMatcher("childPrivateField")));

    assertThat(fields.size(), is(8 + 1));
  }

  @Test
  public void shouldGetOnlyAccessibleFields() {
    List<Field> fields = FieldUtils.getFields(Child.class, false);

    assertThat(fields, hasItem(new FieldMatcher("publicField")));
    assertThat(fields, hasItem(new FieldMatcher("publicStaticField")));
    assertThat(fields.size(), is(2));
  }

  @Test
  public void shouldGetFieldsOfInterface() {
    List<Field> fields = FieldUtils.getFields(InterfaceWithFields.class, true);

    assertThat(fields, hasItem(new FieldMatcher("INTERFACE_FIELD")));
    assertThat(fields.size(), is(1));
  }

  @Test
  public void shouldGetFieldsOfInterfaceImplementation() {
    List<Field> fields = FieldUtils.getFields(InterfaceImplementation.class, true);

    assertThat(fields, hasItem(new FieldMatcher("INTERFACE_FIELD")));
    assertThat(fields.size(), is(1));
  }

  static interface InterfaceWithFields {
    String INTERFACE_FIELD = "foo";
  }

  static class InterfaceImplementation implements InterfaceWithFields {
  }

  static class FieldsWithDifferentModifiers {
    public String publicField;
    protected String protectedField;
    String packageField;
    private String privateField;

    public static String publicStaticField;
    protected static String protectedStaticField;
    static String packageStaticField;
    private static String privateStaticField;
  }

  static class Child extends FieldsWithDifferentModifiers {
    private String childPrivateField;
  }


  static class FieldMatcher extends BaseMatcher<Field> {
    private String name;

    FieldMatcher(String name) {
      this.name = name;
    }

    public boolean matches(Object o) {
      Field field = (Field) o;
      return name.equals(field.getName());
    }

    public void describeTo(Description description) {
      description.appendText("Field with name: ").appendValue(name);
    }
  }
}

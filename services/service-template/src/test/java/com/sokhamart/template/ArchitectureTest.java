package com.sokhamart.template;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(packages = "com.sokhamart.template", importOptions = ImportOption.DoNotIncludeTests.class)
class ArchitectureTest {

    @ArchTest
    static final ArchRule apiMustNotUseRepositories = noClasses()
            .that().resideInAPackage("..api..")
            .should().dependOnClassesThat().haveSimpleNameEndingWith("Repository")
            .because("controllers go through the application layer (docs/adr/0001-package-structure-and-layering.md)");
}

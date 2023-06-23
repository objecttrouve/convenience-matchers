/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2020 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentits;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.junit.Ignore;
import org.junit.Test;
import org.objecttrouve.testing.matchers.ConvenientMatchers;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.aMapLike;
import org.objecttrouve.testing.matchers.customization.MatcherFactory;
import org.objecttrouve.testing.matchers.fluentatts.Attribute;

import java.util.List;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Comparator.comparingInt;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.a;
import static org.objecttrouve.testing.matchers.ConvenientMatchers.anIterableOf;
import static org.objecttrouve.testing.matchers.customization.StringifiersConfig.stringifiers;
import static org.objecttrouve.testing.matchers.fluentatts.Attribute.attribute;


@SuppressWarnings("NewClassNamingConvention")
@Ignore("Failing intentionally.")
public class Examples {

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__ofSize_2__1_actual__consistent_with__exactly() {

        final List<String> strings = singletonList("item");

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    containsString("tem")
                )
                .ofSize(2)
                .exactly()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__ofSize_1__2_actual() {

        final List<String> strings = asList("item", "element");

        assertThat(strings, is(
            anIterableOf(String.class)
                .ofSize(1)
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__1_actual_matching() {

        final List<String> strings = singletonList("item");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                ))
        );
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__3_actual_matching__for_exactly() {

        final List<String> strings = asList("item", "item", "item");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
                .exactly()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__1_actual_matching__2_actual_not_matching() {

        final List<String> strings = asList("element", "element", "item");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__2_matcher_expectations__3_actual_none_matching() {

        final List<String> strings = asList("element", "element", "element");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    containsString("it"),
                    is("item")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__3_item_expectations__2_actual_matching__1_actual_non_matching() {

        final List<String> strings = asList("item", "element", "object");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__3_item_expectations__3_actual_none_matching() {

        final List<String> strings = asList("it", "element", "objection");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "item",
                    "mental",
                    "object"
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__1() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    equalTo("not dump")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__2() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "peaches"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__1_not_matching__3() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .withItems(
                    "fake",
                    "news",
                    "peaches"
                )

        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__all_matching__but_not_exactly() {

        final List<String> strings = asList("fake", "news", "impeachment", "Donald", "Trump", "alternative", "facts");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .exactly()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__1() {

        final List<String> strings = asList("fake", "news", "Donald", "Trump", "impeachment");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__2() {

        final List<String> strings = asList("Donald", "Trump", "fake", "news", "impeachment");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__3() {

        final List<String> strings = asList("impeachment", "Donald", "Trump", "fake", "news");

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .ordered()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__4() {

        final List<String> strings = asList("impeachment", "Donald", "fake", "news", "Trump");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .ordered()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__with_more_actuals_than_expected__exactly() {

        final List<String> strings = asList("alternative", "fake", "news", "facts", "impeachment", "Donald", "Trump");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
                .exactly()

        ));
    }

    @Test
    public void test__matchesSafely__mismatch__mixed_item_and_matcher_expectations__ordered__differently__with_more_actuals_than_expected__1() {

        final List<String> strings = asList("news", "fake", "alternative", "facts", "impeachment", "Donald", "Trump");


        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
                .ordered()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__items_expected_missing() {

        final List<String> strings = asList("alternative", "facts", "impeachment", "Trump");

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__all_items_expected_missing() {

        final List<String> strings = emptyList();

        assertThat(strings, is(
            anIterableOf(String.class)
                .withItems(
                    "fake",
                    "news",
                    "impeachment"
                )
                .withItemsMatching(
                    startsWith("Don"),
                    endsWith("ump")
                )
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__1() {

        final List<String> strings = asList("D", "B", "C");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__2() {

        final List<String> strings = asList("B", "A", "A", "C", "C", "D", "E", "F", "F");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__3() {

        final List<String> strings = asList("A", "B", "A", "C", "C", "D", "E", "F", "F");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__4() {

        final List<String> strings = asList("A", "A", "C", "C", "D", "E", "F", "F", "B");

        assertThat(strings, is(
            anIterableOf(String.class).sorted()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_1() {

        final List<String> strings = asList("AAA", "BB", "CCC", "DDD", "EEEE");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_2() {

        final List<String> strings = asList("AA", "BB", "CCC", "DDD", "EE");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__sorted__unsorted_Collection__using_Comparator_3() {

        final List<String> strings = asList("A", "BB", "CCCC", "DDD", "EEEE");

        assertThat(strings, is(
            anIterableOf(String.class).sorted(comparingInt(String::length))
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__1() {

        final List<String> strings = asList("doubleton", "doubleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__2() {

        final List<String> strings = asList("singleton", "doubleton", "doubleton", "tripleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__3() {

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }

    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__4() {

        final List<String> strings = asList("singleton", "singleton", "doubleton", "tripleton", "tripleton");

        assertThat(strings, is(
            anIterableOf(String.class).unique()
        ));
    }


    @Test
    public void test__matchesSafely__mismatch__unique__Collection_with_duplicates__with_equator_function() {

        final List<String> strings = asList("x", "yy", "zzz", "åå");

        assertThat(strings, is(
            anIterableOf(String.class).unique((x1, x2) -> x1.length() == x2.length())
        ));
    }

    private static final Attribute<String, String> prefix = attribute("prefix", s -> s.substring(0, 2));
    private static final Attribute<String, String> suffix = attribute("suffix", s -> s.substring(2));
    private static final Attribute<String, Integer> length = attribute("length", String::length);


    @Test
    public void heavyMismatch() {

        final List<String> strings = asList(
            "fake",
            "impeachment",
            "Donald",
            "Trump",
            "fake",
            "news"
        );

        assertThat(strings, is(
            anIterableOf(String.class)
                .exactly()
                .sorted()
                .ordered()
                .unique()
                .withItemsMatching(
                    startsWith("Ron"),
                    endsWith("ment")
                )
                .withItems(
                    "true",
                    "news",
                    "impeachment"
                )
        ));
    }

    @Test
    public void full_force_mismatch() {

        final List<String> strings = asList("fake", "impeachment", "Donald", "Trump", "fake", "news");

        assertThat(strings, is(
            anIterableOf(String.class)
                .ofSize(9)
                .sorted()
                .ordered()
                .unique()
                .withItemsMatching(
                    a(String.class).with(prefix, "Ron").with(suffix, "bard"),
                    a(String.class).with(prefix, "Ron").with(suffix, a(String.class).with(length, 500))
                )
                .withItems(
                    "true",
                    "news",
                    "impeachment"
                )
        ));
    }


    private static class Treatment {
        private final String name;
        private final String inventor;

        private Treatment(final String name, final String inventor) {
            this.inventor = inventor;
            this.name = name;
        }

        String getInventor() {
            return inventor;
        }

        String getName() {
            return name;
        }

        @Override
        public String toString() {
            return "Treatment{" +
                "name='" + getName() + '\'' +
                ", inventor='" + getInventor() + '\'' +
                '}';
        }
    }

    private static class Disease {
        private final String name;
        private final Treatment cure;
        private final int duration;

        private Disease(final String name, final Treatment cure, final int duration) {
            this.name = name;
            this.cure = cure;
            this.duration = duration;
        }

        String getName() {
            return name;
        }

        int getDuration() {
            return duration;
        }

        Treatment getCure() {
            return cure;
        }

        @Override
        public String toString() {
            return "Disease{" +
                "name='" + name + '\'' +
                ", cure=" + cure +
                ", duration=" + duration +
                '}';
        }
    }

    private static final Attribute<Disease, String> diseaseName = attribute("disease-name", Disease::getName);
    private static final Attribute<Disease, Integer> duration = attribute("duration", Disease::getDuration);
    private static final Attribute<Disease, Treatment> treatment = attribute("treatment", Disease::getCure);
    private static final Attribute<Treatment, String> treatmentName = attribute("treatment-name", Treatment::getName);
    private static final Attribute<Treatment, String> inventor = attribute("inventor", Treatment::getInventor);

    @Test
    public void flim_and_flam() {

        final Treatment appendixOp = new Treatment("l'appendicectomie", "Avicenne");
        final Disease appendicitis = new Disease("crise d'appendicite aiguë", appendixOp, 1);
        final Treatment coldTherapy = new Treatment("repos au lit", "les ancêtres");
        final Disease cold = new Disease("refroidissement", coldTherapy, 7);
        final Treatment alzheimerTherapy = new Treatment("aucune", "Alzheimer");
        final Disease alzheimer = new Disease("démence d'Alzheimer", alzheimerTherapy, 365 * 20);
        final Treatment naziTherapy = new Treatment("l'éducation", null);
        final Disease nazi = new Disease("Front National", naziTherapy, Integer.MAX_VALUE);
        final Treatment lactoseTherapy = new Treatment("éviter de consommer du lactose en grande quantité", null);
        final Disease lactose = new Disease("intolérance au lactose", lactoseTherapy, 365 * 50);
        final Treatment cancerTherapy = new Treatment("chimiothérapie ", "industrie pharmaceutique");
        final Treatment madCowTherapy = new Treatment("\uD83E\uDD2F", null);
        final Disease madCowDisease = new Disease("encéphalopathie spongiaire bovine", madCowTherapy, -5);
        final Disease cancer = new Disease("cancer", cancerTherapy, 90);
        final List<Disease> diseases = asList(appendicitis, cold, alzheimer, nazi, lactose, alzheimer, madCowDisease);

        assertThat(diseases, is(
            anIterableOf(Disease.class)
                .ofSize(8)
                .ordered()
                .sorted(comparingInt(Disease::getDuration))
                .unique()
                .withItems(appendicitis)
                .withItemsMatching(
                    a(Disease.class)
                        .with(diseaseName, "démence d'Alzheimer")
                        .with(treatment, a(Treatment.class)
                            .with(treatmentName, "aucune")
                            .with(inventor, "Alzheimer"))
                        .with(duration, 365 * 19),
                    a(Disease.class)
                        .with(diseaseName, "refroidissement")
                        .with(treatment, a(Treatment.class)
                            .with(treatmentName, "repos au lit")
                            .with(inventor, "L'Objet Trouvé"))
                        .with(duration, 7),
                    a(Disease.class).with(diseaseName, "intolérance au lactose")
                )
                .withItems(
                    madCowDisease,
                    cancer
                )
                .withItemsMatching(
                    a(Disease.class)
                        .with(diseaseName, "schizophrénie")
                        .with(treatment, a(Treatment.class)
                            .with(treatmentName, "l'éducation")
                            .with(inventor, nullValue()))
                        .with(duration, Integer.MAX_VALUE)
                )
        ));

    }


    @Test
    public void flim_and_flam__debugging__globally() {

        final Function<Treatment, String> treatmentStringFunction = t -> "'" + t.name + "' by '" + t.inventor + "'";
        final MatcherFactory an = ConvenientMatchers.customized()
            .debugging()
            .withStringifiers(
                stringifiers()
                    .withShortStringifier(Treatment.class, t -> t.name)
                    .withDebugStringifier(Treatment.class, treatmentStringFunction)
                    .withShortStringifier(Disease.class, d -> d.name)
                    .withDebugStringifier(Disease.class, d -> "'" + d.name + "' lasting " + d.duration + " days waiting for " + treatmentStringFunction.apply(d.cure))
            ).build();
        final Treatment appendixOp = new Treatment("l'appendicectomie", "Avicenne");
        final Disease appendicitis = new Disease("crise d'appendicite aiguë", appendixOp, 1);
        final Treatment coldTherapy = new Treatment("repos au lit", "les ancêtres");
        final Disease cold = new Disease("refroidissement", coldTherapy, 7);
        final Treatment alzheimerTherapy = new Treatment("aucune", "Alzheimer");
        final Disease alzheimer = new Disease("démence d'Alzheimer", alzheimerTherapy, 365 * 20);
        final Treatment naziTherapy = new Treatment("l'éducation", null);
        final Disease nazi = new Disease("Front National", naziTherapy, Integer.MAX_VALUE);
        final Treatment lactoseTherapy = new Treatment("éviter de consommer du lactose en grande quantité", null);
        final Disease lactose = new Disease("intolérance au lactose", lactoseTherapy, 365 * 50);
        final Treatment cancerTherapy = new Treatment("chimiothérapie ", "industrie pharmaceutique");
        final Treatment madCowTherapy = new Treatment("\uD83E\uDD2F", null);
        final Disease madCowDisease = new Disease("encéphalopathie spongiaire bovine", madCowTherapy, -5);
        final Disease cancer = new Disease("cancer", cancerTherapy, 90);
        final List<Disease> diseases = asList(appendicitis, cold, alzheimer, nazi, lactose, alzheimer, madCowDisease);

        assertThat(diseases, is(
            an.iterableOf(Disease.class)
                .ofSize(8)
                .ordered()
                .sorted(comparingInt(Disease::getDuration))
                .unique()
                .withItems(appendicitis)
                .withItemsMatching(
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "démence d'Alzheimer")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "aucune")
                            .with(inventor, "Alzheimer"))
                        .with(duration, 365 * 19),
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "refroidissement")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "repos au lit")
                            .with(inventor, "L'Objet Trouvé"))
                        .with(duration, 7),
                    an.instanceOf(Disease.class).with(diseaseName, "intolérance au lactose")
                )
                .withItems(
                    madCowDisease,
                    cancer
                )
                .withItemsMatching(
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "schizophrénie")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "l'éducation")
                            .with(inventor, nullValue()))
                        .with(duration, Integer.MAX_VALUE)
                )
        ));

    }


    @Test
    public void flim_and_flam__debugging__focused_on_list() {

        final Function<Treatment, String> treatmentStringFunction = t -> "'" + t.name + "' by '" + t.inventor + "'";
        final MatcherFactory an = ConvenientMatchers.customized()
            .withStringifiers(
                stringifiers()
                    .withShortStringifier(Treatment.class, t -> t.name)
                    .withDebugStringifier(Treatment.class, treatmentStringFunction)
                    .withShortStringifier(Disease.class, d -> d.name)
                    .withDebugStringifier(Disease.class, d -> "'" + d.name + "' lasting " + d.duration + " days waiting for " + treatmentStringFunction.apply(d.cure))
            ).build();
        final Treatment appendixOp = new Treatment("l'appendicectomie", "Avicenne");
        final Disease appendicitis = new Disease("crise d'appendicite aiguë", appendixOp, 1);
        final Treatment coldTherapy = new Treatment("repos au lit", "les ancêtres");
        final Disease cold = new Disease("refroidissement", coldTherapy, 7);
        final Treatment alzheimerTherapy = new Treatment("aucune", "Alzheimer");
        final Disease alzheimer = new Disease("démence d'Alzheimer", alzheimerTherapy, 365 * 20);
        final Treatment naziTherapy = new Treatment("l'éducation", null);
        final Disease nazi = new Disease("Front National", naziTherapy, Integer.MAX_VALUE);
        final Treatment lactoseTherapy = new Treatment("éviter de consommer du lactose en grande quantité", null);
        final Disease lactose = new Disease("intolérance au lactose", lactoseTherapy, 365 * 50);
        final Treatment cancerTherapy = new Treatment("chimiothérapie ", "industrie pharmaceutique");
        final Treatment madCowTherapy = new Treatment("\uD83E\uDD2F", null);
        final Disease madCowDisease = new Disease("encéphalopathie spongiaire bovine", madCowTherapy, -5);
        final Disease cancer = new Disease("cancer", cancerTherapy, 90);
        final List<Disease> diseases = asList(appendicitis, cold, alzheimer, nazi, lactose, alzheimer, madCowDisease);

        assertThat(diseases, is(
            an.iterableOf(Disease.class)
                .debugging()
                .ofSize(8)
                .ordered()
                .sorted(comparingInt(Disease::getDuration))
                .unique()
                .withItems(appendicitis)
                .withItemsMatching(
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "démence d'Alzheimer")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "aucune")
                            .with(inventor, "Alzheimer"))
                        .with(duration, 365 * 19),
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "refroidissement")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "repos au lit")
                            .with(inventor, "L'Objet Trouvé"))
                        .with(duration, 7),
                    an.instanceOf(Disease.class).with(diseaseName, "intolérance au lactose")
                )
                .withItems(
                    madCowDisease,
                    cancer
                )
                .withItemsMatching(
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "schizophrénie")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "l'éducation")
                            .with(inventor, nullValue()))
                        .with(duration, Integer.MAX_VALUE)
                )
        ));

    }


    @Test
    public void flim_and_flam__debugging__focused_on_item() {

        final Function<Treatment, String> treatmentStringFunction = t -> "'" + t.name + "' by '" + t.inventor + "'";
        final MatcherFactory an = ConvenientMatchers.customized()
            .withStringifiers(
                stringifiers()
                    .withShortStringifier(Treatment.class, t -> t.name)
                    .withDebugStringifier(Treatment.class, treatmentStringFunction)
                    .withShortStringifier(Disease.class, d -> d.name)
                    .withDebugStringifier(Disease.class, d -> "'" + d.name + "' lasting " + d.duration + " days waiting for " + treatmentStringFunction.apply(d.cure))
            ).build();
        final Treatment appendixOp = new Treatment("l'appendicectomie", "Avicenne");
        final Disease appendicitis = new Disease("crise d'appendicite aiguë", appendixOp, 1);
        final Treatment coldTherapy = new Treatment("repos au lit", "les ancêtres");
        final Disease cold = new Disease("refroidissement", coldTherapy, 7);
        final Treatment alzheimerTherapy = new Treatment("aucune", "Alzheimer");
        final Disease alzheimer = new Disease("démence d'Alzheimer", alzheimerTherapy, 365 * 20);
        final Treatment naziTherapy = new Treatment("l'éducation", null);
        final Disease nazi = new Disease("Front National", naziTherapy, Integer.MAX_VALUE);
        final Treatment lactoseTherapy = new Treatment("éviter de consommer du lactose en grande quantité", null);
        final Disease lactose = new Disease("intolérance au lactose", lactoseTherapy, 365 * 50);
        final Treatment cancerTherapy = new Treatment("chimiothérapie ", "industrie pharmaceutique");
        final Treatment madCowTherapy = new Treatment("\uD83E\uDD2F", null);
        final Disease madCowDisease = new Disease("encéphalopathie spongiaire bovine", madCowTherapy, -5);
        final Disease cancer = new Disease("cancer", cancerTherapy, 90);
        final List<Disease> diseases = asList(appendicitis, cold, alzheimer, nazi, lactose, alzheimer, madCowDisease);

        assertThat(diseases, is(
            an.iterableOf(Disease.class)
                .ofSize(8)
                .ordered()
                .sorted(comparingInt(Disease::getDuration))
                .unique()
                .withItems(appendicitis)
                .withItemsMatching(
                    an.instanceOf(Disease.class)
                        .debugging() // No impact, need to switch on debugging for the list, too.
                        .with(diseaseName, "démence d'Alzheimer")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "aucune")
                            .with(inventor, "Alzheimer"))
                        .with(duration, 365 * 19),
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "refroidissement")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "repos au lit")
                            .with(inventor, "L'Objet Trouvé"))
                        .with(duration, 7),
                    an.instanceOf(Disease.class).with(diseaseName, "intolérance au lactose")
                )
                .withItems(
                    madCowDisease,
                    cancer
                )
                .withItemsMatching(
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "schizophrénie")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "l'éducation")
                            .with(inventor, nullValue()))
                        .with(duration, Integer.MAX_VALUE)
                )
        ));

    }


    @Test
    public void flim_and_flam__debugging__focused_on_list_and_single_item() {

        final Function<Treatment, String> treatmentStringFunction = t -> "'" + t.name + "' by '" + t.inventor + "'";
        final MatcherFactory an = ConvenientMatchers.customized()
            .debugging()
            .withStringifiers(
                stringifiers()
                    .withShortStringifier(Treatment.class, t -> t.name)
                    .withDebugStringifier(Treatment.class, treatmentStringFunction)
                    .withShortStringifier(Disease.class, d -> d.name)
                    .withDebugStringifier(Disease.class, d -> "'" + d.name + "' lasting " + d.duration + " days waiting for " + treatmentStringFunction.apply(d.cure))
            ).build();
        final Treatment appendixOp = new Treatment("l'appendicectomie", "Avicenne");
        final Disease appendicitis = new Disease("crise d'appendicite aiguë", appendixOp, 1);
        final Treatment coldTherapy = new Treatment("repos au lit", "les ancêtres");
        final Disease cold = new Disease("refroidissement", coldTherapy, 7);
        final Treatment alzheimerTherapy = new Treatment("aucune", "Alzheimer");
        final Disease alzheimer = new Disease("démence d'Alzheimer", alzheimerTherapy, 365 * 20);
        final Treatment naziTherapy = new Treatment("l'éducation", null);
        final Disease nazi = new Disease("Front National", naziTherapy, Integer.MAX_VALUE);
        final Treatment lactoseTherapy = new Treatment("éviter de consommer du lactose en grande quantité", null);
        final Disease lactose = new Disease("intolérance au lactose", lactoseTherapy, 365 * 50);
        final Treatment cancerTherapy = new Treatment("chimiothérapie ", "industrie pharmaceutique");
        final Treatment madCowTherapy = new Treatment("\uD83E\uDD2F", null);
        final Disease madCowDisease = new Disease("encéphalopathie spongiaire bovine", madCowTherapy, -5);
        final Disease cancer = new Disease("cancer", cancerTherapy, 90);
        final List<Disease> diseases = asList(appendicitis, cold, alzheimer, nazi, lactose, alzheimer, madCowDisease);

        assertThat(diseases, is(
            an.iterableOf(Disease.class)
                .ofSize(8)
                .ordered()
                .sorted(comparingInt(Disease::getDuration))
                .unique()
                .withItems(appendicitis)
                .withItemsMatching(
                    an.instanceOf(Disease.class)
                        .debugging() // No impact, need to switch on debugging for the list, too.
                        .with(diseaseName, "démence d'Alzheimer")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "aucune")
                            .with(inventor, "Alzheimer"))
                        .with(duration, 365 * 19),
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "refroidissement")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "repos au lit")
                            .with(inventor, "L'Objet Trouvé"))
                        .with(duration, 7),
                    an.instanceOf(Disease.class).with(diseaseName, "intolérance au lactose")
                )
                .withItems(
                    madCowDisease,
                    cancer
                )
                .withItemsMatching(
                    an.instanceOf(Disease.class)
                        .with(diseaseName, "schizophrénie")
                        .with(treatment, an.instanceOf(Treatment.class)
                            .with(treatmentName, "l'éducation")
                            .with(inventor, nullValue()))
                        .with(duration, Integer.MAX_VALUE)
                )
        ));

    }

    @Test
    public void customized() {

        final Function<String, String> truncated = s -> s.substring(0, 3) + "...";
        final Function<String, String> detailed = s -> s + " (" + s.length() + ")";
        final Attribute<String, String> prefix = attribute("prefix", s -> s.substring(0, 4));

        final MatcherFactory an = ConvenientMatchers.customized()
            .debugging()
            .withAsciiSymbols()
            .withStringifiers(stringifiers()
                .withShortStringifier(String.class, truncated)
                .withDebugStringifier(String.class, detailed)
            )
            .build();

        final List<String> items = asList("This", "prints", "pretty", ".....");

        assertThat(items, is(
            an.iterableOf(String.class)
                .withItemsMatching(
                    an.instanceOf(String.class)
                        .with(prefix, "Xxxx"))));
    }


    @Test
    public void mapMatcherKeyValMismatch(){
        final Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        assertThat(map, is(
                aMapLike(map)
                        .withKeyVal("key2", "value1")
                        .withKeyVal("key1", "value2")
                        .withKeyVal("key2", "value2")
        ));
    }

    @Test
    public void mapMatcherKeyValMatcherMismatch(){
        final Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        assertThat(map, is(
                aMapLike(map)
                        .withKeyValMatching(equalTo("key2"), equalTo("value1"))
                        .withKeyValMatching(equalTo("key2"), equalTo("value2"))
        ));
    }

    @Test
    public void mapMatcherSizeMismatch(){
        final Map<String, String> map = new HashMap<>();
        map.put("key1", "value1");
        map.put("key2", "value2");

        assertThat(map, is(
                aMapLike(map)
                        .ofSize(4)
        ));
    }

    @Test
    public void mapMatcherSortMismatch(){
        final Map<String, String> map = new LinkedHashMap<>();
        map.put("key2", "value2");
        map.put("key1", "value1");

        assertThat(map, is(
                aMapLike(map)
                        .sorted()
                        .ofSize(3)
                        .withKeyVal("key0", "val0")
                        .withKeyValMatching(equalTo("key3"), equalTo("val3"))
        ));
    }

    @Test
    public void mapMatcherMultiple(){
        final Map<String, String> map = new LinkedHashMap<>();
        map.put("key2", "value2");
        map.put("key1", "value1");

        assertThat(map, is(
                aMapLike(map)
                        .sorted()
        ));
    }


}

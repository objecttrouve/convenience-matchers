/*
 * Released under the terms of the MIT License.
 *
 * Copyright (c) 2017 objecttrouve.org <un.object.trouve@gmail.com>
 *
 */

package org.objecttrouve.testing.matchers.fluentatts;


import net.sf.cglib.core.CodeGenerationException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;


public class TrackerTest {

    private static class Something {

        private final String it;

        private Something(final String it) {
            this.it = it;
        }

        public String getIt() {
            return it;
        }

        public String getItAgain() {
            return it;
        }
    }

    private static class IdleThing {

        public void procrastinate() {
            // Relax...
        }
    }

    private static class NestedThing {

        private final Something something;

        private NestedThing(final Something something) {
            this.something = something;
        }

        public Something something() {
            return something;
        }
    }


    @Test
    public void testTrackNothing() throws Exception {
        final Something theSomething = new Something("the value");
        final TrackingTree trackingTree = new TrackingTree();
        Tracker.track(theSomething, trackingTree);

        assertFalse(trackingTree.method().isPresent());

        // It's not happening...
        // tracked.getIt();

        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(0));
    }

    @Test
    public void testTrackSimpleGetter() throws Exception {

        final Something theSomething = new Something("the value");

        final TrackingTree trackingTree = new TrackingTree();
        final Something tracked = Tracker.track(theSomething, trackingTree);

        tracked.getIt();

        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(1));
        assertThat(trackedCall.get(0).method().get().getName(), is("getIt"));
        assertThat(trackedCall.get(0).tracked().size(), is(0));

    }

    @Test
    public void testTrackSomethingVoid() throws Exception {

        final IdleThing idle = new IdleThing();

        final TrackingTree trackingTree = new TrackingTree();
        final IdleThing tracked = Tracker.track(idle, trackingTree);

        tracked.procrastinate();

        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(1));
        assertThat(trackedCall.get(0).method().get().getName(), is("procrastinate"));
        assertThat(trackedCall.get(0).tracked().size(), is(0));

    }


    @Test
    public void testTrackSomethingNested() throws Exception {

        final NestedThing nestedThing = new NestedThing(new Something("Yippieh!!"));
        final TrackingTree trackingTree = new TrackingTree();
        final NestedThing tracked = Tracker.track(nestedThing, trackingTree);

        final String it = tracked.something().getIt();

        assertThat(it, is("Yippieh!!"));
        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(1));
        final TrackingTree nextTree = trackedCall.get(0);
        assertThat(nextTree.method().get().getName(), is("something"));
        final List<TrackingTree> trackedFurther = nextTree.tracked();
        assertThat(trackedFurther.size(), is(1));
        assertThat(trackedFurther.get(0).method().get().getName(), is("getIt"));
    }

    private static class IntimateThing {

        private String secretly() {
            return "Dumdidum...";
        }
    }

    @Test
    public void testTrackSomethingPrivate() throws Exception {

        final IntimateThing intimate = new IntimateThing();
        final TrackingTree trackingTree = new TrackingTree();
        final IntimateThing tracked = Tracker.track(intimate, trackingTree);

        final String topSecret = tracked.secretly();

        assertThat(topSecret, is("Dumdidum..."));
        final List<TrackingTree> trackedCall = trackingTree.tracked();
        // We just don't track private things.
        assertThat(trackedCall.size(), is(0));

    }

    @Test
    public void testTrackLambda() throws Exception {

        final NestedThing nestedThing = new NestedThing(new Something("Yippieh!!"));
        final TrackingTree trackingTree = new TrackingTree();
        final NestedThing tracked = Tracker.track(nestedThing, trackingTree);
        final Function<NestedThing, String> ref = (nested) -> nested.something().getIt();

        final String it = ref.apply(tracked);

        assertThat(it, is("Yippieh!!"));
        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(1));
        final TrackingTree nextTree = trackedCall.get(0);
        assertThat(nextTree.method().get().getName(), is("something"));

    }

    @Test
    public void testTrackMethodReference() throws Exception {

        final NestedThing nestedThing = new NestedThing(new Something("Yippieh!!"));
        final TrackingTree trackingTree = new TrackingTree();
        final NestedThing tracked = Tracker.track(nestedThing, trackingTree);
        final Function<NestedThing, Something> ref = NestedThing::something;

        final Something something = ref.apply(tracked);

        assertThat(something.getIt(), is("Yippieh!!"));
        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(1));
        final TrackingTree nextTree = trackedCall.get(0);
        assertThat(nextTree.method().get().getName(), is("something"));

    }

    private static class Personality {
        private final String identity;

        private Personality(final String identity) {
            this.identity = identity;
        }

        public String getIdentity() {
            return identity;
        }
    }

    private static class MultiplePersonality {
        private final List<Personality> personalities;

        private MultiplePersonality(final Personality... personalities) {
            this.personalities = new ArrayList<>(Arrays.asList(personalities));
        }

        public List<Personality> getPersonalities() {
            return personalities;
        }
    }

    @Test
    public void testTrackAcrossACollection() throws Exception {

        final MultiplePersonality clones = new MultiplePersonality(new Personality("Sarah"), new Personality("Cosima"), new Personality("Alison"));
        final TrackingTree trackingTree = new TrackingTree();
        final MultiplePersonality tracked = Tracker.track(clones, trackingTree);
        final Function<MultiplePersonality, String> lamb = original -> original.getPersonalities().get(1).getIdentity();

        final String identity = lamb.apply(tracked);

        assertThat(identity, is("Cosima"));
        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(1));
        assertThat(trackedCall.get(0).method().get().getName(), is("getPersonalities"));
        assertThat(trackedCall.get(0).tracked().get(0).method().get().getName(), is("get"));
        assertThat(trackedCall.get(0).tracked().get(0).tracked().get(0).method().get().getName(), is("getIdentity"));

    }

    private static class Untrackable {
        private final List<String> untrackable;

        private Untrackable(final String... untrackable) {
            /*
            * It's an implementation detail of that list to cause IllegalAccessErrors on proxy creation.
            * At least until either their or our implementation is fixed.
            * Until that happens it servs to reproduce the issue.
            */
            this.untrackable = Arrays.asList(untrackable);
        }

        public List<String> getUntrackable() {
            return untrackable;
        }
    }

    @Test
    public void testTrackErringOnProxyCreation() throws Exception {

        final Untrackable untrackable = new Untrackable("The", "untrackable", "cannot", "be", "tracked");
        final TrackingTree trackingTree = new TrackingTree();
        final Untrackable tracked = Tracker.track(untrackable, trackingTree);
        final Function<Untrackable, String> call = untr -> untr.getUntrackable().get(1);

        final String result = call.apply(tracked);

        assertThat(result, is("untrackable"));
        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(1));
        assertThat(trackedCall.get(0).method().get().getName(), is("getUntrackable"));
        assertThat(trackedCall.get(0).tracked().size(), is(1));
        assertThat(trackedCall.get(0).tracked().get(0).error().get(), instanceOf(CodeGenerationException.class));
    }

    @Test
    public void testTrackingStopsWhenSwitchedOff() throws Exception {

        final TrackingTree trackingTree = new TrackingTree();
        final Something tracked = Tracker.track(new Something("whatever"), trackingTree);
        final Function<Something, String> call = Something::getIt;

        final String result = call.apply(tracked);
        trackingTree.stopTracking();
        tracked.getItAgain();

        assertThat(result, is("whatever"));
        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(1));
        assertThat(trackedCall.get(0).method().get().getName(), is("getIt"));
        assertThat(trackedCall.get(0).tracked().size(), is(0));
    }

    @Test
    public void testTrackingStopsWhenSwitchedOffAndThingsAreNested() throws Exception {

        final TrackingTree trackingTree = new TrackingTree();
        final NestedThing tracked = Tracker.track(new NestedThing(new Something("whatever")), trackingTree);
        final List<Something> trackedOnFirstLevel = new LinkedList<>();
        final Function<NestedThing, String> call = nt -> {
            final Something something = nt.something();
            trackedOnFirstLevel.add(something);
            return something.getIt();
        };

        final String result = call.apply(tracked);
        trackingTree.stopTracking();
        trackedOnFirstLevel.get(0).getItAgain();

        assertThat(result, is("whatever"));
        final List<TrackingTree> trackedCall = trackingTree.tracked();
        assertThat(trackedCall.size(), is(1));
        assertThat(trackedCall.get(0).method().get().getName(), is("something"));
        assertThat(trackedCall.get(0).tracked().size(), is(1));
        assertThat(trackedCall.get(0).tracked().get(0).method().get().getName(), is("getIt"));
        assertThat(trackedCall.get(0).tracked().get(0).tracked().size(), is(0));

    }
}
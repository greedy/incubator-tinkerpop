package com.tinkerpop.gremlin.neo4j;

import com.tinkerpop.gremlin.AbstractGraphProvider;
import com.tinkerpop.gremlin.LoadGraphWith;
import com.tinkerpop.gremlin.neo4j.structure.Neo4jGraph;
import com.tinkerpop.gremlin.structure.Graph;
import org.apache.commons.configuration.Configuration;
import org.neo4j.graphdb.DynamicLabel;

import java.io.File;
import java.util.Random;

/**
 * @author Stephen Mallette (http://stephen.genoprime.com)
 */
public abstract class AbstractNeo4jGraphProvider extends AbstractGraphProvider {

    @Override
    public void clear(final Graph g, final Configuration configuration) throws Exception {
        if (null != g) {
            if (g.features().graph().supportsTransactions())
                g.tx().rollback();
            g.close();
        }

        if (configuration.containsKey("gremlin.neo4j.directory")) {
            // this is a non-in-sideEffects configuration so blow away the directory
            final File graphDirectory = new File(configuration.getString("gremlin.neo4j.directory"));
            deleteDirectory(graphDirectory);
        }
    }

    @Override
    public void loadGraphData(final Graph g, final LoadGraphWith loadGraphWith, final Class testClass, final String testName) {
        if (loadGraphWith != null) this.createIndices((Neo4jGraph) g, loadGraphWith.value());
        super.loadGraphData(g, loadGraphWith, testClass, testName);
    }

    /**
     * Removes characters that aren't acceptable in a file path (mostly for windows).
     */
    protected static String cleanPathSegment(final String toClean) {
        final String cleaned = toClean.replaceAll("[.\\\\/:*?\"<>|\\[\\]\\(\\)]", "");
        if(cleaned.length() == 0) throw new IllegalStateException("Path segment " + toClean + " has not valid characters and is thus empty");
        return cleaned;
    }

    private void createIndices(final Neo4jGraph g, final LoadGraphWith.GraphData graphData) {
        final Random random = new Random();
        final int pick = random.nextInt(3);
        //final int pick = 2;
        if (graphData.equals(LoadGraphWith.GraphData.GRATEFUL)) {
            if (pick == 1) {
                g.tx().readWrite();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("artist")).on("name").create();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("song")).on("name").create();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("song")).on("songType").create();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("song")).on("performances").create();
                g.tx().commit();
            } else if (pick == 2) {
                g.tx().readWrite();
                g.getBaseGraph().index().getNodeAutoIndexer().setEnabled(true);
                if (random.nextBoolean())
                    g.getBaseGraph().index().getNodeAutoIndexer().startAutoIndexingProperty("name");
                if (random.nextBoolean())
                    g.getBaseGraph().index().getNodeAutoIndexer().startAutoIndexingProperty("songType");
                if (random.nextBoolean())
                    g.getBaseGraph().index().getNodeAutoIndexer().startAutoIndexingProperty("performances");
                g.tx().commit();
            }
        } else if (graphData.equals(LoadGraphWith.GraphData.MODERN)) {
            if (pick == 1) {
                g.tx().readWrite();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("person")).on("name").create();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("person")).on("age").create();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("software")).on("name").create();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("software")).on("lang").create();
                g.tx().commit();
            } else if (pick == 2) {
                g.tx().readWrite();
                g.getBaseGraph().index().getNodeAutoIndexer().setEnabled(true);
                if (random.nextBoolean())
                    g.getBaseGraph().index().getNodeAutoIndexer().startAutoIndexingProperty("name");
                if (random.nextBoolean())
                    g.getBaseGraph().index().getNodeAutoIndexer().startAutoIndexingProperty("age");
                if (random.nextBoolean())
                    g.getBaseGraph().index().getNodeAutoIndexer().startAutoIndexingProperty("lang");
                g.tx().commit();
            }
        } else if (graphData.equals(LoadGraphWith.GraphData.CLASSIC)) {
            if (pick == 1) {
                g.tx().readWrite();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("vertex")).on("name").create();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("vertex")).on("age").create();
                if (random.nextBoolean())
                    g.getBaseGraph().schema().indexFor(DynamicLabel.label("vertex")).on("lang").create();
                g.tx().commit();
            } else if (pick == 2) {
                g.tx().readWrite();
                g.getBaseGraph().index().getNodeAutoIndexer().setEnabled(true);
                if (random.nextBoolean())
                    g.getBaseGraph().index().getNodeAutoIndexer().startAutoIndexingProperty("name");
                if (random.nextBoolean())
                    g.getBaseGraph().index().getNodeAutoIndexer().startAutoIndexingProperty("age");
                if (random.nextBoolean())
                    g.getBaseGraph().index().getNodeAutoIndexer().startAutoIndexingProperty("lang");
                g.tx().commit();
            }
        } else {
            // TODO: add CREW work here.
            // TODO: add meta_property indices when meta_property graph is provided
            //throw new RuntimeException("Could not load graph with " + graphData);
        }
    }
}

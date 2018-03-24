package ca.ualberta.cs.w18t11.whoselineisitanyway.model.elastic;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Represents that which can be stored in Elasticsearch.
 *
 * @author Samuel Dolha
 * @version 1.1
 */
public interface Elastic
{
    /**
     * @return The object's elastic ID.
     */
    @Nullable
    String getElasticId();

    /**
     * Sets the object's elastic ID.
     *
     * @param id The object's new elastic ID.
     */
    void setElasticId(@NonNull final String id);
}

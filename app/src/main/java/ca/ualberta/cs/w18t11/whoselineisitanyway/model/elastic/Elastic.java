package ca.ualberta.cs.w18t11.whoselineisitanyway.model.elastic;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Represents that which can be stored in elasticsearch.
 *
 * @author Samuel Dolha
 * @version 1.0
 */
public interface Elastic
{
    @Nullable
    String getElasticId();

    void setElasticId(@NonNull final String id);
}

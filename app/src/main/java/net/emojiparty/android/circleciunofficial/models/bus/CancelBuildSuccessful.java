package net.emojiparty.android.circleciunofficial.models.bus;

import net.emojiparty.android.circleciunofficial.models.Build;

/**
 * Created by Siena on 10/11/2014.
 */
public class CancelBuildSuccessful {

    private Build build;

    public CancelBuildSuccessful(Build build) {
        this.build = build;
    }

    public Build getBuild() {
        return build;
    }
}

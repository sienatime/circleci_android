package net.emojiparty.android.circleciunofficial.models.bus;

import net.emojiparty.android.circleciunofficial.models.Build;

/**
 * Created by Siena on 10/11/2014.
 */
public class BuildsLoadedEvent {
    private Build[] builds;

    public BuildsLoadedEvent(Build[] builds) {
        this.builds = builds;
    }

    public Build[] getBuilds() {
        return builds;
    }
}

package net.jordanlabs.bot.service;

import net.jordanlabs.bot.domain.Feature;
import net.jordanlabs.bot.domain.JdkProgress;
import net.jordanlabs.bot.domain.JdkRelease;
import net.jordanlabs.bot.domain.Milestone;
import net.jordanlabs.bot.domain.Phase;
import net.jordanlabs.bot.domain.Schedule;
import net.jordanlabs.bot.extractor.feature.FeatureExtractor;
import net.jordanlabs.bot.extractor.feature.Features;
import net.jordanlabs.bot.provider.HtmlParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static net.jordanlabs.bot.domain.Milestone.MILESTONE_DATE_FORMAT;

@Singleton
public class JdkProgressFetcher {
    private final HtmlParser htmlParser;

    @Inject
    public JdkProgressFetcher(final HtmlParser htmlParser) {
        this.htmlParser = htmlParser;
    }

    public JdkProgress fetchProgress(final String jdkProjectUrl) throws IOException {
        final Document doc = htmlParser.loadDocumentFrom(jdkProjectUrl);
        final Element releases = doc.selectFirst("div#main > ul");
        final Deque<JdkRelease> jdkReleases = new LinkedList<>();
        for (Element release : releases.select("li")) {
            final Element releaseLink = release.selectFirst("a");
            final String releaseUrl = releaseLink.attr("abs:href");
            final String releaseNumber = releaseLink.text();
            final String currentMilestone = release.ownText().replaceAll("[()]", ""); // eg. GA 2018/03/20 OR in development

            final Document releaseDoc = Jsoup.connect(releaseUrl).get();
            final Schedule schedule = fetchSchedule(releaseDoc);
            final List<Milestone> milestones = fetchMilestones(releaseDoc);
            final List<Feature> features = fetchFeatures(releaseDoc);
            final LocalDateTime lastUpdated = lastUpdated(releaseDoc);
            jdkReleases.addFirst(new JdkRelease(
                releaseUrl,
                releaseNumber,
                currentMilestone,
                schedule,
                milestones,
                features,
                lastUpdated
            ));
        }
        return new JdkProgress(jdkReleases);
    }

    private Schedule fetchSchedule(final Document releaseDoc) {
        // sometimes a schedule will be proposed
        final Element scheduleLink = releaseDoc.selectFirst("h2#Schedule > a");
        if (scheduleLink != null) {
            final String schedule = scheduleLink.ownText().replaceAll("[()]", "");
            final String scheduleUrl = scheduleLink.attr("abs:href");
            final String scheduleUrl2 = scheduleLink.absUrl("href");
            final boolean isProposed = schedule.equalsIgnoreCase("proposed");
            return new Schedule(isProposed, scheduleUrl);
        }
        return new Schedule();
    }

    private LocalDateTime lastUpdated(final Document releaseDoc) {
        final Element lastUpdated = releaseDoc.selectFirst("div.last-update");
        if (lastUpdated != null) {
            final String updateTimestamp = lastUpdated.text().replace("Last update: ", "");
            return LocalDateTime.parse(updateTimestamp, DateTimeFormatter.ofPattern("yyyy/M/d HH:mm z"));
        }
        return LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
    }

    private List<Milestone> fetchMilestones(final Document releaseDoc) {
        final List<Milestone> milestones = new ArrayList<>();
        for (final Element milestone : releaseDoc.select("tr.milestone")) {
           final LocalDate milestoneDate = LocalDate.parse(milestone.child(0).text(), MILESTONE_DATE_FORMAT);
           final Element milestoneLink = milestone.selectFirst("a");
           final Element milestoneComment = milestone.selectFirst("td.comment");
           milestones.add(new Milestone(
               milestoneDate,
               Phase.fromDescription(Objects.requireNonNullElse(milestoneLink, milestoneComment).text()),
               milestoneLink != null ? milestoneLink.attr("abs:href") : "")
           );
        }
        return milestones;
    }

    private List<Feature> fetchFeatures(final Document releaseDoc) {
        final FeatureExtractor featureExtractor = Features.createExtractor(releaseDoc);
        return featureExtractor.extractFeatures();
    }
}

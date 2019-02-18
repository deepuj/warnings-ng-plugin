package io.jenkins.plugins.analysis.core.graphs;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import com.google.common.annotations.VisibleForTesting;

import io.jenkins.plugins.analysis.core.model.AnalysisResult;

/**
 * Determines whether a build result is too old in order to be considered for a trend graph.
 *
 * @author Ullrich Hafner
 */
public class ResultTime {
    private final LocalDate today;

    /**
     * Creates a new instance of {@link ResultTime}. The current date from the system clock in the default time-zone is
     * used to initialize this instance.
     */
    public ResultTime() {
        this(LocalDate.now());
    }

    /**
     * Creates a new instance of {@link ResultTime}.
     *
     * @param now
     *         the date representing today
     */
    @VisibleForTesting
    ResultTime(final LocalDate now) {
        today = now;
    }

    /**
     * Returns whether the specified build result is too old in order to be considered for the trend graph.
     *
     * @param configuration
     *         configuration of the model
     * @param analysisRun
     *         the results of a analysis run
     *
     * @return {@code true} if the build is too old
     */
    public boolean isResultTooOld(final ChartModelConfiguration configuration, final AnalysisResult analysisRun) {
        return configuration.isDayCountDefined() && computeDayDelta(analysisRun) > configuration.getDayCount();
    }

    private long computeDayDelta(final AnalysisResult result) {
        return Math.abs(ChronoUnit.DAYS.between(toLocalDate(result.getBuild().getTimeInMillis()), today));
    }

    private LocalDate toLocalDate(final long timeInMillis) {
        return Instant.ofEpochMilli(timeInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}

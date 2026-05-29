package com.Sarthak.course_recommender.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.Sarthak.course_recommender.model.ClassGroup;
import com.Sarthak.course_recommender.model.TimetableSlot;
import com.Sarthak.course_recommender.model.enums.ActivityType;
import com.Sarthak.course_recommender.repository.ClassGroupRepository;
import com.Sarthak.course_recommender.repository.TimetableSlotRepository;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class TimetableService {

    private final TimetableSlotRepository timetableSlotRepository;
    private final ClassGroupRepository classGroupRepository;

    // Fixed time constants
    private static final LocalTime DINNER_START = LocalTime.of(18, 0);
    private static final LocalTime DINNER_END = LocalTime.of(19, 0);
    private static final LocalTime GAMES_START = LocalTime.of(19, 0);
    private static final LocalTime GAMES_END = LocalTime.of(20, 0);
    private static final LocalTime CLASS_START = LocalTime.of(20, 0);
    private static final LocalTime CLASS_END = LocalTime.of(21, 30);
    private static final LocalTime FREE_START = LocalTime.of(21, 30);
    private static final LocalTime FREE_END = LocalTime.of(22, 0);
    private static final LocalTime MOVIE_START = LocalTime.of(19, 0);
    private static final LocalTime MOVIE_END = LocalTime.of(21, 0);
    private static final LocalTime MOVIE_FREE_END = LocalTime.of(22, 0);
    private static final LocalTime SLEEP_TIME = LocalTime.of(22, 0);

    // ── Read ────────────────────────────────────────────────

    public List<TimetableSlot> getHostelWideTimetable() {
        return timetableSlotRepository.findByClassGroupIsNull();
    }

    public List<TimetableSlot> getClassGroupTimetable(Long classGroupId) {
        ClassGroup group = classGroupRepository.findById(classGroupId)
                .orElseThrow(() -> new com.Sarthak.course_recommender.service.exception.ResourceNotFoundException(
                        "ClassGroup not found with id: " + classGroupId));
        return timetableSlotRepository.findByClassGroup(group);
    }

    public List<TimetableSlot> getDailySchedule(DayOfWeek day) {
        return timetableSlotRepository.findByDayOfWeek(day);
    }

    // ── Generation ──────────────────────────────────────────

    /**
     * Generates the full timetable for the entire session.
     * Called once. Can be called again to regenerate (wipes previous).
     *
     * Logic:
     * - Pick a random day for movie night
     * - For every day of the week:
     * → Dinner (hostel-wide, every day)
     * → If movie night day: Movie Night + extended Free Time
     * → Otherwise: Games + Class slots per group + Free Time
     * → Sleep marker (hostel-wide, every day)
     */
    @Transactional
    public DayOfWeek generateTimetable() {
        // Wipe existing timetable
        timetableSlotRepository.deleteAll();

        // Pick random movie night day
        DayOfWeek[] allDays = DayOfWeek.values();
        DayOfWeek movieNightDay = allDays[new Random().nextInt(allDays.length)];

        List<ClassGroup> allGroups = classGroupRepository.findAll();

        for (DayOfWeek day : allDays) {

            // Dinner — every day, hostel-wide
            saveSlot(null, day, DINNER_START, DINNER_END, ActivityType.DINNER);

            if (day == movieNightDay) {
                // Movie night displaces games + class
                saveSlot(null, day, MOVIE_START, MOVIE_END, ActivityType.MOVIE_NIGHT);
                saveSlot(null, day, MOVIE_END, MOVIE_FREE_END, ActivityType.FREE_TIME);
            } else {
                // Regular day
                saveSlot(null, day, GAMES_START, GAMES_END, ActivityType.GAMES);

                // Self-study class slot per class group
                for (ClassGroup group : allGroups) {
                    saveSlot(group, day, CLASS_START, CLASS_END, ActivityType.CLASS);
                }

                saveSlot(null, day, FREE_START, FREE_END, ActivityType.FREE_TIME);
            }

            // Sleep — every day, hostel-wide
            saveSlot(null, day, SLEEP_TIME, SLEEP_TIME, ActivityType.SLEEP);
        }

        return movieNightDay; // return so the caller knows which day was picked
    }

    private void saveSlot(ClassGroup classGroup, DayOfWeek day,
            LocalTime start, LocalTime end, ActivityType type) {
        TimetableSlot slot = TimetableSlot.builder()
                .classGroup(classGroup)
                .dayOfWeek(day)
                .startTime(start)
                .endTime(end)
                .activityType(type)
                .weeklyRecurring(type != ActivityType.MOVIE_NIGHT)
                .build();
        timetableSlotRepository.save(slot);
    }
}
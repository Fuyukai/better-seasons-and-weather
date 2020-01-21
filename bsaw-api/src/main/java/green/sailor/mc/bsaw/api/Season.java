/*
 * This file is part of BSAW-API.
 *
 * BSAW is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BSAW is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with BSAW.  If not, see <https://www.gnu.org/licenses/>.
 */

package green.sailor.mc.bsaw.api;

import org.jetbrains.annotations.NotNull;

/**
 * Enumeration of the valid seasons. Each season lasts for 25 days, with 5 transitional days at the
 * end.
 */
public enum Season {
    /** Winter; the coldest season. */
    WINTER,
    /** Spring; warmer than winter but colder than summer. */
    SPRING,
    /** Summer; the warmest season. */
    SUMMER,
    /** Autumn; colder than summer but warmer than winter. */
    AUTUMN;

    /** The length of a single MC day, in ticks. */
    public static final long DAY_LENGTH = 24000L;
    /** The length of a single MC season, in ticks. */
    public static final long SEASON_LENGTH = DAY_LENGTH * 30;
    /** The length of a single MC year, in ticks. */
    public static final long YEAR_LENGTH = DAY_LENGTH * 120;

    /**
     * Gets a season from the specified world time.
     */
    @NotNull
    public static Season seasonFromTime(long worldTime) {
        long cycled = worldTime % YEAR_LENGTH;
        if (0L <= cycled && cycled < DAY_LENGTH * 30) {
            return Season.SPRING;
        } else if (DAY_LENGTH * 30 <= cycled && cycled < DAY_LENGTH * 60) {
            return Season.SUMMER;
        } else if (DAY_LENGTH * 60 <= cycled && cycled < DAY_LENGTH * 80) {
            return Season.AUTUMN;
        } else if (DAY_LENGTH * 90 <= cycled) {
            return Season.WINTER;
        } else {
            throw new IllegalArgumentException("Somehow invalid cycled" + cycled);
        }
    }
}

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

import java.security.SecureRandom;
import java.util.Random;

/**
 * Represents extended biome information about a biome.
 */
public class BiomeExtendedInfo {
    // this is a SecureRandom instead of a Random so users can't cheese
    /** The random class used for getting temperature variations. */
    protected static Random tempRandom = new SecureRandom();

    protected static double nextDouble(double low, double high) {
        return low + (high - low) * tempRandom.nextDouble();
    }

    @Override
    public String toString() {
        return "BiomeInfoExtended{" +
            "identifier='" + identifier + '\'' +
            ", highTempRange=" + highTempRange +
            ", lowTempRange=" + lowTempRange +
            '}';
    }

    /**
     * An enumeration of the list of rainfall types.
     *
     * <ul>
     *     <li>{@link RainfallType#NONE} - No rainfall.</li>
     *     <li>{@link RainfallType#RAIN} - Rain.</li>
     *     <li>{@link RainfallType#SNOW} - Snow.</li>
     * </ul>
     */
    public enum RainfallType {
        NONE,
        RAIN,
        SNOW
    }

    // default impls

    /**
     * Represents a very hot biome, that gets significantly cooler in the winter,
     * and cold overnight.
     */
    public static class VeryHotBiomeExtendedInfo extends BiomeExtendedInfo {
        public VeryHotBiomeExtendedInfo(String identifier) {
            super(identifier, 30.0d, 45.0d);
        }

        @Override
        public double nextRandomTemperature(@NotNull Season season, boolean isNight) {
            // winter is MUCH colder
            if (season == Season.WINTER) {
                if (isNight) return BiomeExtendedInfo.nextDouble(-25.0D, -10.0D);
                return BiomeExtendedInfo.nextDouble(-10.0D, 0.0D);
            }

            // nights are also much colder
            if (isNight) {
                return BiomeExtendedInfo.nextDouble(0.0D, 10.0D);
            }
            return super.nextRandomTemperature(season, false);
        }

        @NotNull
        @Override
        public RainfallType rainfallTypeFor(double temperature) {
            if (temperature < -3.0d) {
                return RainfallType.SNOW;
            } else {
                return RainfallType.NONE;
            }
        }
    }

    /**
     * Represents a moderately hot biome, which gets hot in the day, and cold at winter and night.
     */
    public static class HotBiomeExtendedInfo extends BiomeExtendedInfo {
        public HotBiomeExtendedInfo(String identifier) {
            super(identifier, 20.0d, 35.0d);
        }

        @Override
        public double nextRandomTemperature(@NotNull Season season, boolean isNight) {
            // winter is MUCH colder
            if (season == Season.WINTER) {
                if (isNight) return BiomeExtendedInfo.nextDouble(-10.0D, -5.0D);
                return BiomeExtendedInfo.nextDouble(-3.0D, 6.0D);
            }

            // nights are also much colder
            if (isNight) {
                return BiomeExtendedInfo.nextDouble(3.0D, 12.0D);
            }
            return super.nextRandomTemperature(season, false);
        }
    }

    /**
     * Represents a cold biome that gets significantly warmer in the summer, but cold otherwise.
     */
    public static class ColdBiomeExtendedInfo extends BiomeExtendedInfo {
        public ColdBiomeExtendedInfo(String identifier) {
            super(identifier, -5.0D, 5.0D);
        }

        @Override
        public double nextRandomTemperature(@NotNull Season season, boolean isNight) {
            if (season == Season.SUMMER) {
                return BiomeExtendedInfo.nextDouble(0.0d, 8d);
            }
            return super.nextRandomTemperature(season, isNight);
        }
    }

    /**
     * Represents a very cold biome.
     */
    public static class VeryColdBiomeExtendedInfo extends BiomeExtendedInfo {
        public VeryColdBiomeExtendedInfo(String identifier) {
            super(identifier, -15.0D, 0.0D);
        }

        @Override
        public double nextRandomTemperature(@NotNull Season season, boolean isNight) {
            if (season == Season.SUMMER) {
                return BiomeExtendedInfo.nextDouble(-5.0d, 3d);
            }
            return super.nextRandomTemperature(season, isNight);
        }
    }

    /** The identifier for this biome. */
    @NotNull public final String identifier;

    /** The high temperature range. */
    public final double highTempRange;
    /** The low temperature range. */
    public final double lowTempRange;

    /**
     * @param identifier The identifier for this biome info.
     * @param lowTempRange The low boundary for the spring temperature.
     * @param highTempRange The high boundary for the spring temperature.
     */
    public BiomeExtendedInfo(
        @NotNull String identifier, double lowTempRange, double highTempRange
    ) {
        this.identifier = identifier;
        this.lowTempRange = lowTempRange;
        this.highTempRange = highTempRange;
    }

    /**
     * Gets the rainfall type for the specified temperature.
     *
     * @param temperature The biome temperature.
     * @return A {@link RainfallType}.
     */
    @NotNull public RainfallType rainfallTypeFor(double temperature) {
        if (temperature <= 0.0d) {
            return RainfallType.SNOW;
        } else if (temperature > 0.0d && temperature <= 35.0d) {
            return RainfallType.RAIN;
        } else {
            // TODO
            return RainfallType.RAIN;
        }
    }

    /**
     * @return The next random temperature for the biome.
     */
    // default impl
    public double nextRandomTemperature(@NotNull Season season, boolean isNight) {
        double offset = 0.0D;
        if (isNight) {
            // nights are cooler by ~3.0C
            offset -= 3;
        }
        if (season == Season.WINTER) {
            // winters are cooler by ~15c
            offset -= 15;
        } else if (season == Season.SUMMER) {
            // summers are hotter by ~10c
            offset += 10d;
        }

        return nextDouble(lowTempRange + offset, highTempRange + offset);
    }

}

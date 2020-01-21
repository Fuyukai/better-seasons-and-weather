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

package green.sailor.mc.bsaw.api.component;

/**
 * The interface for player temperature information.
 */
public interface PlayerTemperatureProvider {
    /**
     * @return The ideal body temperature of the player.
     */
    default double getIdealTemperature() {
        return 37.5D;
    }

    default double getSweatDissipiationTemperature() {
        return 27.0D;
    }

    /**
     * @return The minimum temperature the player can be before becoming hypothermic.
     */
    default double getHypothermicBound() {
        return 30.0D;
    }

    /**
     * @return If the player is hypothermic.
     */
    default boolean isHypothermic() {
        return getTemperature() < getHypothermicBound();
    }

    /**
     * @return The maximum temperature the player can be before becoming hyperthermic.
     */
    default double getHyperthermicBound() {
        return 41.0D;
    }

    /**
     * @return If the player is hyperthermic.
     */
    default boolean isHyperthermic() {
        return getTemperature() > getHyperthermicBound();
    }

    /**
     * @return The internal temperature of the player.
     */
    double getTemperature();
}

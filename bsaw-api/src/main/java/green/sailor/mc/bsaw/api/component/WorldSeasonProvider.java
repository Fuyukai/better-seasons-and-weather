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

import green.sailor.mc.bsaw.api.Season;
import green.sailor.mc.bsaw.api.Time;

/**
 * The interface for World season information.
 */
public interface WorldSeasonProvider {
    /**
     * Gets the temperature for the specified Biome identifier.
     *
     * @param identifier The identifier for the biome.
     * @return The current temperature.
     */
    double getBiomeTemp(String identifier);

    /**
     * Updates all biome temperature for the specified {@link Season} and {@link Time}.
     */
    void updateAllBiomeTemps(Season season, Time at);
}

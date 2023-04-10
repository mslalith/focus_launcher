package dev.mslalith.focuslauncher.core.data.dto

import dev.mslalith.focuslauncher.core.data.database.entities.AddressRoom
import dev.mslalith.focuslauncher.core.data.network.entities.AddressResponse
import dev.mslalith.focuslauncher.core.model.place.Address

internal fun AddressResponse.toAddress(): Address = Address(
    state = state,
    country = country
)

internal fun AddressRoom.toAddress(): Address = Address(
    state = state,
    country = country
)

internal fun AddressResponse.toAddressRoom(): AddressRoom = AddressRoom(
    houseNumber = houseNumber,
    road = road,
    village = village,
    suburb = suburb,
    postCode = postCode,
    county = county,
    stateDistrict = stateDistrict,
    state = state,
    iso3166 = iso3166,
    country = country,
    countryCode = countryCode
)

package com.sa.nafhasehaprovider.entity.response.sockeEmmitModel

/**
< Vampire >
 */


class TrackerModel {
    var tracker_id: Int = 0
    var user_id: Int = 0
}

class TrackerLocation {
    var client_id: Int = 0
    var p_latitude: Double = 0.toDouble()
    var p_longitude: Double = 0.toDouble()


    constructor() {}

    constructor(client_id: Int,p_lat: Double, p_lng: Double) {
        this.client_id = client_id
        this.p_latitude = p_lat
        this.p_longitude = p_lng
    }

}

export const actionTypes = {
    SET_BOT_DETAILS: 'SET_BOT_DETAILS',
    SET_BOTS : 'SET_BOTS',
    SET_SCREEN_TESTS : 'SET_SCREEN_TESTS',
    SET_LIFECYCLE_STATUSES : 'SET_LIFECYCLE_STATUSES'
}

const getDefaultFetchPost = (body) => {
    let object = { method: 'POST',
                              headers: {
                                  'Accept': 'application/json',
                                  'Content-Type': 'application/json'
                              }
                     };

    if(!_.isUndefined(body)) {
        object.body = JSON.stringify(body);
    }

    return object;
}

export const getBots = () => {
    return (dispatch) => {
        return fetch("/api/bot").then((response) => {
            if (response.ok) {
                response.json().then(json => dispatch(setBots(json)));
            }
        })
    }
}

export const getBotDetails = (botName) => {
    return (dispatch) => {
        return fetch("/api/bot/details/" + botName).then((response) => {
            if (response.ok) {
                response.json().then(json => dispatch(setBotDetails(json)));
            }
        })
    }
}

export const getProcessingLifecycleStatuses = () => {
    return (dispatch) => {
        return fetch("/api/event/lifecycle-status/").then((response) => {
            if (response.ok) {
                response.json().then(json => dispatch(setProcessingLifecycleStatuses(json)));
            }
        })
    }
}

export const getAssumedScreenTests = () => {
    return (dispatch) => {
        return fetch("/api/event/screen-test/").then((response) => {
            if (response.ok) {
                response.json().then(json => dispatch(setAssumedScreenTests(json)));
            }
        })
    }
}

export const submitEvent = (newEvent) => {
    return (dispatch) => {
        return fetch("/api/event/", getDefaultFetchPost(newEvent)).then((response) => {
            if (response.ok) {
            }
        })
    }
}

export const setBots = (bots) => ({
  type: actionTypes.SET_BOTS,
  bots
});

export const setBotDetails = (botDetails) => ({
  type: actionTypes.SET_BOT_DETAILS,
  botDetails
});

export const setAssumedScreenTests = (enums) => ({
  type: actionTypes.SET_SCREEN_TESTS,
  enums
});

export const setProcessingLifecycleStatuses = (enums) => ({
  type: actionTypes.SET_LIFECYCLE_STATUSES,
  enums
});

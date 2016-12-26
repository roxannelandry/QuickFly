homeApp.factory('homeResource', [ "homeService", function($scope) {

	function formatDate(date) {
		var day = padLeftWithZero(date.getDate());
		var month = padLeftWithZero(date.getMonth() + 1);
		var year = date.getFullYear();

		var formatDate = year + "-" + month + "-" + day;

		return formatDate;
	}

	function padLeftWithZero(value) {
		if (value > 0 && value <= 9) {
			return "0" + value;
		}
		return value;
	}

	function getSystemDate() {
		return new Date();
	}

	function addDays(date, days) {
		var result = new Date(date);
		result.setDate(result.getDate() + days);
		return result;
	}

	function removeDays(date, days) {
		var result = new Date(date);
		result.setDate(result.getDate() - days);
		return result;
	}

	function roundWeightToUpperHalf(luggageWeight) {
		return Math.ceil(luggageWeight * 2) / 2;
	}

	function showWarningPrice(flights, luggageWeight) {
		for ( var flight in flights) {
			if (flights[flight].flightCategory == 'AIR_LOURD' && luggageWeight > 65 && !flights[flight].hasToBeDisable) {
				return true;
			}
		}
		return false;
	}

	function showWarningFilter(flights) {
		if (flights.hasFilteredResult == true) {
			return true;
		}
		return false;
	}

	return {
		formatDate : formatDate,
		getSystemDate : getSystemDate,
		addDays : addDays,
		removeDays : removeDays,
		roundWeightToUpperHalf : roundWeightToUpperHalf,
		showWarningPrice : showWarningPrice,
		showWarningFilter : showWarningFilter
	};
	
}]);
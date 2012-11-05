/*
 * Copyright (C) 2012 Carl Wilson <carl@openplanetsfoundation.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// jquery extension 
$.extend({
	// Function to get the URL vars
	getUrlVars : function() {
		var vars = [], hash;
		var hashes = window.location.href.slice(
				window.location.href.indexOf('?') + 1).split('&');
		for ( var i = 0; i < hashes.length; i++) {
			hash = hashes[i].split('=');
			vars.push(hash[0]);
			vars[hash[0]] = hash[1];
		}
		return vars;
	},
	// Function to get them by name
	getUrlVar : function(name) {
		return $.getUrlVars()[name];
	}
});

/**
 * Set the passed item as selected.
 * @param itemId the item to select
 */
function selNavbarItem(itemId) {
	$('#navbar').find('a').each(function() {
		$(this).removeClass("select");
		if ($(this).attr("id") == itemId) $(this).addClass("select");
	});
}

/**
 * Sets up the modal prompt window
 */
function setupOverlay() {
    // Set up the overlay
    $('#prompt').overlay({
      top : 260,
      // some mask tweaks suitable for modal dialogs
      mask : {
        color : '#fff',
        loadSpeed : 200,
        opacity : 0.5
      },
      closeOnClick : false
    });
}

function displayData() {
	$("*").find('[data-value]').each(
		function() {
			formatData($(this));
		}
	);
}

function formatData(element) {
	switch($(element).attr("data-type"))
	{
	case "date":
		$(element).text($(element).attr("data-name") + ": " + formatLongDate($(element).attr("data-value")));
		break;
	case "bytes":
		$(element).text($(element).attr("data-name") + ": " + bytesToSize($(element).attr("data-value")));
		break;
	case "percent":
		$(element).text($(element).attr("data-name") + ": " + ($(element).attr("data-value") * 100).toFixed(0) + "%");
		break;
	case "format":
		$(element).text($(element).attr("data-name") + ": " + formatFormatURI($(element).attr("data-value")));
		break;
	default:
		$(element).text($(element).attr("data-name") + ": " + $(element).attr("data-value"));
	}
}

function formatFormatURI(format) {
	return format.split(":").pop();
}
/**
 * Function to format a javascript date and zero pad the hours and minutes.
 * @param longDate the numeric long date to format
 * @returns {String} the formatted date
 */
function formatLongDate(longDate) {
	longDate = parseInt(longDate);
	return (longDate >= 0) ? formatDate(new Date(longDate)) : formatDate(date);
}

/**
 * Function to format a javascript date and zero pad the hours and minutes.
 * @param date the date to format
 * @returns {String} the formatted date
 */
function formatDate(date) {
	var hours = "" + date.getHours();
	if (hours.length < 2) hours = "0" + hours;
	var minutes = "" + date.getMinutes();
	if (minutes.length < 2) minutes = "0" + minutes;
	return date.toDateString() + " " + hours + ":" + minutes;
}

/**
 * Convert number of bytes into human readable format
 *
 * @param integer bytes     Number of bytes to convert
 * @param integer precision Number of digits after the decimal separator
 * @return string
 */
function bytesToSize(bytes)
{  
    var kilobyte = 1024;
    var megabyte = kilobyte * 1024;
    var gigabyte = megabyte * 1024;
    var terabyte = gigabyte * 1024;
    var precision = 2;
    if ((bytes >= 0) && (bytes < kilobyte)) {
        return bytes + ' B';
 
    } else if ((bytes >= kilobyte) && (bytes < megabyte)) {
        return (bytes / kilobyte).toFixed(precision) + ' KB';
 
    } else if ((bytes >= megabyte) && (bytes < gigabyte)) {
        return (bytes / megabyte).toFixed(precision) + ' MB';
 
    } else if ((bytes >= gigabyte) && (bytes < terabyte)) {
        return (bytes / gigabyte).toFixed(precision) + ' GB';
 
    } else if (bytes >= terabyte) {
        return (bytes / terabyte).toFixed(precision) + ' TB';
 
    } else {
        return bytes + ' B';
    }
}

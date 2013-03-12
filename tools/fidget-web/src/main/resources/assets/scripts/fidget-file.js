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
function submitSignature() {
	var chunksize = (1024 * 64); // We're sending up to 64K of data
	var sigFile = document.getElementById("sig-file").files[0]; // FileList
	var datFile = document.getElementById("data-file").files[0]; // FileList
																	// object
	var formData = new FormData();
	formData.append("sigFile", sigFile); // Add sig file to form data
	formData.append("sigName", sigFile.name); // Get sig file name
	formData.append("datFile", sliceFile(datFile, 0, chunksize)); // Add data file
																	// slice to
																	// form data
	formData.append("datName", datFile.name); // Data File name
	$.ajax({
		url : "http://localhost:8080/fidget",
		type : "POST",
		data : formData,
		dataType: "html",
		processData: false,
		contentType: false,
		success : function(data, success) {
			$("#results").empty();
			$("#results").append(data);
		}
	});
}

/**
 * Cross browser function to take a slice of a file and return the slice as a
 * blob.
 * 
 * TODO: Cross browser testing (IE ?)
 */
function sliceFile(file, start, stop) {
	if (!file) // If file is null then return;
		return;

	if (!start) // If no start then start at the begining
		var start = 0;

	if ((!stop) || (stop > file.size)) // If size null or > file.size
		var stop = file.size + 1; // then = size +1

	// Cross browser madness for slicing the file into a blob and returning it
	if (file.webkitSlice) {
		return file.webkitSlice(start, stop);
	} else if (file.mozSlice) {
		return file.mozSlice(start, stop);
	} else
		return file;
}

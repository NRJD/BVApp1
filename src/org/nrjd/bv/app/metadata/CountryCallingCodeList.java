/*
 * Copyright (C) 2015 ISKCON New Rajapur Jagannatha Dham.
 *
 * This file is part of Bhakti Vriksha application.
 */
package org.nrjd.bv.app.metadata;


import java.util.Set;

// Package scope class.
class CountryCallingCodeList {
    private static void populateOrderedCallingCodes(Set<CountryCallingCode> callingCodes) {
        callingCodes.add(new CountryCallingCode(91, "IN", "India"));
        callingCodes.add(new CountryCallingCode(1, "US", "United States of America"));
        callingCodes.add(new CountryCallingCode(44, "UK", "United Kingdom"));
        callingCodes.add(new CountryCallingCode(86, "CN", "China"));
        // populateTestCallingCodes(orderedCallingCodes);
    }

    private static void populateTestCallingCodes(Set<CountryCallingCode> callingCodes) {
        callingCodes.add(new CountryCallingCode(801, "81", "Temp country calling code with the medium text"));
        callingCodes.add(new CountryCallingCode(802, "82", "Temp country calling code with a longer text for the country name value"));
        for (int index = 0; index < 50; index++) {
            char ch1 = ((index < 25) ? 'Y' : 'Z');
            char ch2 = (char) (65 + (index % 25));
            String tempCountryCode = "" + ch1 + ch2;
            callingCodes.add(new CountryCallingCode(900 + index, tempCountryCode, "Temp country " + tempCountryCode));
        }
    }
        
    // TODO: Localize based on the user locale. Reload if user changes the locale.
    static final CountryCallingCode[] CALLING_CODES = new CountryCallingCode[]{
            new CountryCallingCode(1, "AG", "Antigua and Barbuda"), // Country Code
            new CountryCallingCode(1, "AI", "Anguilla"), // Country Code
            new CountryCallingCode(1, "AS", "American Samoa"), // Country Code
            new CountryCallingCode(1, "BB", "Barbados"), // Country Code
            new CountryCallingCode(1, "BM", "Bermuda"), // Country Code
            new CountryCallingCode(1, "BS", "Bahamas"), // Country Code
            new CountryCallingCode(1, "CA", "Canada"), // Country Code
            new CountryCallingCode(1, "DM", "Dominica"), // Country Code
            new CountryCallingCode(1, "DO", "Dominican Republic"), // Country Code
            new CountryCallingCode(1, "GD", "Grenada"), // Country Code
            new CountryCallingCode(1, "GU", "Guam"), // Country Code
            new CountryCallingCode(1, "JM", "Jamaica"), // Country Code
            new CountryCallingCode(1, "KN", "Saint Kitts and Nevis"), // Country Code
            new CountryCallingCode(1, "KY", "Cayman Islands"), // Country Code
            new CountryCallingCode(1, "LC", "Saint Lucia"), // Country Code
            new CountryCallingCode(1, "MP", "Northern Mariana Islands"), // Country Code
            new CountryCallingCode(1, "MS", "Montserrat"), // Country Code
            new CountryCallingCode(1, "PR", "Puerto Rico"), // Country Code
            new CountryCallingCode(1, "SX", "Sint Maarten"), // Country Code
            new CountryCallingCode(1, "TC", "Turks and Caicos Islands"), // Country Code
            new CountryCallingCode(1, "TT", "Trinidad and Tobago"), // Country Code
            new CountryCallingCode(1, "US", "United States"), // Country Code
            new CountryCallingCode(1, "VC", "Saint Vincent and the Grenadines"), // Country Code
            new CountryCallingCode(1, "VG", "British Virgin Islands"), // Country Code
            new CountryCallingCode(1, "VI", "U.S. Virgin Islands"), // Country Code
            new CountryCallingCode(7, "KZ", "Kazakhstan"), // Country Code
            new CountryCallingCode(7, "RU", "Russia"), // Country Code
            new CountryCallingCode(20, "EG", "Egypt"), // Country Code
            new CountryCallingCode(27, "ZA", "South Africa"), // Country Code
            new CountryCallingCode(30, "GR", "Greece"), // Country Code
            new CountryCallingCode(31, "NL", "Netherlands"), // Country Code
            new CountryCallingCode(32, "BE", "Belgium"), // Country Code
            new CountryCallingCode(33, "FR", "France"), // Country Code
            new CountryCallingCode(34, "ES", "Spain"), // Country Code
            new CountryCallingCode(36, "HU", "Hungary"), // Country Code
            new CountryCallingCode(39, "IT", "Italy"), // Country Code
            new CountryCallingCode(40, "RO", "Romania"), // Country Code
            new CountryCallingCode(41, "CH", "Switzerland"), // Country Code
            new CountryCallingCode(43, "AT", "Austria"), // Country Code
            new CountryCallingCode(44, "GB", "United Kingdom"), // Country Code
            new CountryCallingCode(44, "GG", "Guernsey"), // Country Code
            new CountryCallingCode(44, "IM", "Isle of Man"), // Country Code
            new CountryCallingCode(44, "JE", "Jersey"), // Country Code
            new CountryCallingCode(45, "DK", "Denmark"), // Country Code
            new CountryCallingCode(46, "SE", "Sweden"), // Country Code
            new CountryCallingCode(47, "NO", "Norway"), // Country Code
            new CountryCallingCode(47, "SJ", "Svalbard and Jan Mayen"), // Country Code
            new CountryCallingCode(48, "PL", "Poland"), // Country Code
            new CountryCallingCode(49, "DE", "Germany"), // Country Code
            new CountryCallingCode(51, "PE", "Peru"), // Country Code
            new CountryCallingCode(52, "MX", "Mexico"), // Country Code
            new CountryCallingCode(53, "CU", "Cuba"), // Country Code
            new CountryCallingCode(54, "AR", "Argentina"), // Country Code
            new CountryCallingCode(55, "BR", "Brazil"), // Country Code
            new CountryCallingCode(56, "CL", "Chile"), // Country Code
            new CountryCallingCode(57, "CO", "Colombia"), // Country Code
            new CountryCallingCode(58, "VE", "Venezuela"), // Country Code
            new CountryCallingCode(60, "MY", "Malaysia"), // Country Code
            new CountryCallingCode(61, "AU", "Australia"), // Country Code
            new CountryCallingCode(61, "CC", "Cocos Islands"), // Country Code
            new CountryCallingCode(61, "CX", "Christmas Island"), // Country Code
            new CountryCallingCode(62, "ID", "Indonesia"), // Country Code
            new CountryCallingCode(63, "PH", "Philippines"), // Country Code
            new CountryCallingCode(64, "NZ", "New Zealand"), // Country Code
            new CountryCallingCode(64, "PN", "Pitcairn"), // Country Code
            new CountryCallingCode(65, "SG", "Singapore"), // Country Code
            new CountryCallingCode(66, "TH", "Thailand"), // Country Code
            new CountryCallingCode(81, "JP", "Japan"), // Country Code
            new CountryCallingCode(82, "KR", "South Korea"), // Country Code
            new CountryCallingCode(84, "VN", "Vietnam"), // Country Code
            new CountryCallingCode(86, "CN", "China"), // Country Code
            new CountryCallingCode(90, "TR", "Turkey"), // Country Code
            new CountryCallingCode(91, "IN", "India"), // Country Code
            new CountryCallingCode(92, "PK", "Pakistan"), // Country Code
            new CountryCallingCode(93, "AF", "Afghanistan"), // Country Code
            new CountryCallingCode(94, "LK", "Sri Lanka"), // Country Code
            new CountryCallingCode(95, "MM", "Myanmar"), // Country Code
            new CountryCallingCode(98, "IR", "Iran"), // Country Code
            new CountryCallingCode(211, "SS", "South Sudan"), // Country Code
            new CountryCallingCode(212, "EH", "Western Sahara"), // Country Code
            new CountryCallingCode(212, "MA", "Morocco"), // Country Code
            new CountryCallingCode(213, "DZ", "Algeria"), // Country Code
            new CountryCallingCode(216, "TN", "Tunisia"), // Country Code
            new CountryCallingCode(218, "LY", "Libya"), // Country Code
            new CountryCallingCode(220, "GM", "Gambia"), // Country Code
            new CountryCallingCode(221, "SN", "Senegal"), // Country Code
            new CountryCallingCode(222, "MR", "Mauritania"), // Country Code
            new CountryCallingCode(223, "ML", "Mali"), // Country Code
            new CountryCallingCode(224, "GN", "Guinea"), // Country Code
            new CountryCallingCode(225, "CI", "Ivory Coast"), // Country Code
            new CountryCallingCode(226, "BF", "Burkina Faso"), // Country Code
            new CountryCallingCode(227, "NE", "Niger"), // Country Code
            new CountryCallingCode(228, "TG", "Togo"), // Country Code
            new CountryCallingCode(229, "BJ", "Benin"), // Country Code
            new CountryCallingCode(230, "MU", "Mauritius"), // Country Code
            new CountryCallingCode(231, "LR", "Liberia"), // Country Code
            new CountryCallingCode(232, "SL", "Sierra Leone"), // Country Code
            new CountryCallingCode(233, "GH", "Ghana"), // Country Code
            new CountryCallingCode(234, "NG", "Nigeria"), // Country Code
            new CountryCallingCode(235, "TD", "Chad"), // Country Code
            new CountryCallingCode(236, "CF", "Central African Republic"), // Country Code
            new CountryCallingCode(237, "CM", "Cameroon"), // Country Code
            new CountryCallingCode(238, "CV", "Cape Verde"), // Country Code
            new CountryCallingCode(239, "ST", "Sao Tome and Principe"), // Country Code
            new CountryCallingCode(240, "GQ", "Equatorial Guinea"), // Country Code
            new CountryCallingCode(241, "GA", "Gabon"), // Country Code
            new CountryCallingCode(242, "CG", "Republic of the Congo"), // Country Code
            new CountryCallingCode(243, "CD", "Democratic Republic of the Congo"), // Country Code
            new CountryCallingCode(244, "AO", "Angola"), // Country Code
            new CountryCallingCode(245, "GW", "Guinea-Bissau"), // Country Code
            new CountryCallingCode(246, "IO", "British Indian Ocean Territory"), // Country Code
            new CountryCallingCode(248, "SC", "Seychelles"), // Country Code
            new CountryCallingCode(249, "SD", "Sudan"), // Country Code
            new CountryCallingCode(250, "RW", "Rwanda"), // Country Code
            new CountryCallingCode(251, "ET", "Ethiopia"), // Country Code
            new CountryCallingCode(252, "SO", "Somalia"), // Country Code
            new CountryCallingCode(253, "DJ", "Djibouti"), // Country Code
            new CountryCallingCode(254, "KE", "Kenya"), // Country Code
            new CountryCallingCode(255, "TZ", "Tanzania"), // Country Code
            new CountryCallingCode(256, "UG", "Uganda"), // Country Code
            new CountryCallingCode(257, "BI", "Burundi"), // Country Code
            new CountryCallingCode(258, "MZ", "Mozambique"), // Country Code
            new CountryCallingCode(260, "ZM", "Zambia"), // Country Code
            new CountryCallingCode(261, "MG", "Madagascar"), // Country Code
            new CountryCallingCode(262, "RE", "Reunion"), // Country Code
            new CountryCallingCode(262, "YT", "Mayotte"), // Country Code
            new CountryCallingCode(263, "ZW", "Zimbabwe"), // Country Code
            new CountryCallingCode(264, "NA", "Namibia"), // Country Code
            new CountryCallingCode(265, "MW", "Malawi"), // Country Code
            new CountryCallingCode(266, "LS", "Lesotho"), // Country Code
            new CountryCallingCode(267, "BW", "Botswana"), // Country Code
            new CountryCallingCode(268, "SZ", "Swaziland"), // Country Code
            new CountryCallingCode(269, "KM", "Comoros"), // Country Code
            new CountryCallingCode(290, "SH", "Saint Helena"), // Country Code
            new CountryCallingCode(291, "ER", "Eritrea"), // Country Code
            new CountryCallingCode(297, "AW", "Aruba"), // Country Code
            new CountryCallingCode(298, "FO", "Faroe Islands"), // Country Code
            new CountryCallingCode(299, "GL", "Greenland"), // Country Code
            new CountryCallingCode(350, "GI", "Gibraltar"), // Country Code
            new CountryCallingCode(351, "PT", "Portugal"), // Country Code
            new CountryCallingCode(352, "LU", "Luxembourg"), // Country Code
            new CountryCallingCode(353, "IE", "Ireland"), // Country Code
            new CountryCallingCode(354, "IS", "Iceland"), // Country Code
            new CountryCallingCode(355, "AL", "Albania"), // Country Code
            new CountryCallingCode(356, "MT", "Malta"), // Country Code
            new CountryCallingCode(357, "CY", "Cyprus"), // Country Code
            new CountryCallingCode(358, "FI", "Finland"), // Country Code
            new CountryCallingCode(359, "BG", "Bulgaria"), // Country Code
            new CountryCallingCode(370, "LT", "Lithuania"), // Country Code
            new CountryCallingCode(371, "LV", "Latvia"), // Country Code
            new CountryCallingCode(372, "EE", "Estonia"), // Country Code
            new CountryCallingCode(373, "MD", "Moldova"), // Country Code
            new CountryCallingCode(374, "AM", "Armenia"), // Country Code
            new CountryCallingCode(375, "BY", "Belarus"), // Country Code
            new CountryCallingCode(376, "AD", "Andorra"), // Country Code
            new CountryCallingCode(377, "MC", "Monaco"), // Country Code
            new CountryCallingCode(378, "SM", "San Marino"), // Country Code
            new CountryCallingCode(379, "VA", "Vatican"), // Country Code
            new CountryCallingCode(380, "UA", "Ukraine"), // Country Code
            new CountryCallingCode(381, "RS", "Serbia"), // Country Code
            new CountryCallingCode(382, "ME", "Montenegro"), // Country Code
            new CountryCallingCode(383, "XK", "Kosovo"), // Country Code
            new CountryCallingCode(385, "HR", "Croatia"), // Country Code
            new CountryCallingCode(386, "SI", "Slovenia"), // Country Code
            new CountryCallingCode(387, "BA", "Bosnia and Herzegovina"), // Country Code
            new CountryCallingCode(389, "MK", "Macedonia"), // Country Code
            new CountryCallingCode(420, "CZ", "Czech Republic"), // Country Code
            new CountryCallingCode(421, "SK", "Slovakia"), // Country Code
            new CountryCallingCode(423, "LI", "Liechtenstein"), // Country Code
            new CountryCallingCode(500, "FK", "Falkland Islands"), // Country Code
            new CountryCallingCode(501, "BZ", "Belize"), // Country Code
            new CountryCallingCode(502, "GT", "Guatemala"), // Country Code
            new CountryCallingCode(503, "SV", "El Salvador"), // Country Code
            new CountryCallingCode(504, "HN", "Honduras"), // Country Code
            new CountryCallingCode(505, "NI", "Nicaragua"), // Country Code
            new CountryCallingCode(506, "CR", "Costa Rica"), // Country Code
            new CountryCallingCode(507, "PA", "Panama"), // Country Code
            new CountryCallingCode(508, "PM", "Saint Pierre and Miquelon"), // Country Code
            new CountryCallingCode(509, "HT", "Haiti"), // Country Code
            new CountryCallingCode(590, "BL", "Saint Barthelemy"), // Country Code
            new CountryCallingCode(590, "MF", "Saint Martin"), // Country Code
            new CountryCallingCode(591, "BO", "Bolivia"), // Country Code
            new CountryCallingCode(592, "GY", "Guyana"), // Country Code
            new CountryCallingCode(593, "EC", "Ecuador"), // Country Code
            new CountryCallingCode(595, "PY", "Paraguay"), // Country Code
            new CountryCallingCode(597, "SR", "Suriname"), // Country Code
            new CountryCallingCode(598, "UY", "Uruguay"), // Country Code
            new CountryCallingCode(599, "AN", "Netherlands Antilles"), // Country Code
            new CountryCallingCode(599, "CW", "Curacao"), // Country Code
            new CountryCallingCode(670, "TL", "East Timor"), // Country Code
            new CountryCallingCode(672, "AQ", "Antarctica"), // Country Code
            new CountryCallingCode(673, "BN", "Brunei"), // Country Code
            new CountryCallingCode(674, "NR", "Nauru"), // Country Code
            new CountryCallingCode(675, "PG", "Papua New Guinea"), // Country Code
            new CountryCallingCode(676, "TO", "Tonga"), // Country Code
            new CountryCallingCode(677, "SB", "Solomon Islands"), // Country Code
            new CountryCallingCode(678, "VU", "Vanuatu"), // Country Code
            new CountryCallingCode(679, "FJ", "Fiji"), // Country Code
            new CountryCallingCode(680, "PW", "Palau"), // Country Code
            new CountryCallingCode(681, "WF", "Wallis and Futuna"), // Country Code
            new CountryCallingCode(682, "CK", "Cook Islands"), // Country Code
            new CountryCallingCode(683, "NU", "Niue"), // Country Code
            new CountryCallingCode(685, "WS", "Samoa"), // Country Code
            new CountryCallingCode(686, "KI", "Kiribati"), // Country Code
            new CountryCallingCode(687, "NC", "New Caledonia"), // Country Code
            new CountryCallingCode(688, "TV", "Tuvalu"), // Country Code
            new CountryCallingCode(689, "PF", "French Polynesia"), // Country Code
            new CountryCallingCode(690, "TK", "Tokelau"), // Country Code
            new CountryCallingCode(691, "FM", "Micronesia"), // Country Code
            new CountryCallingCode(692, "MH", "Marshall Islands"), // Country Code
            new CountryCallingCode(850, "KP", "North Korea"), // Country Code
            new CountryCallingCode(852, "HK", "Hong Kong"), // Country Code
            new CountryCallingCode(853, "MO", "Macao"), // Country Code
            new CountryCallingCode(855, "KH", "Cambodia"), // Country Code
            new CountryCallingCode(856, "LA", "Laos"), // Country Code
            new CountryCallingCode(880, "BD", "Bangladesh"), // Country Code
            new CountryCallingCode(886, "TW", "Taiwan"), // Country Code
            new CountryCallingCode(960, "MV", "Maldives"), // Country Code
            new CountryCallingCode(961, "LB", "Lebanon"), // Country Code
            new CountryCallingCode(962, "JO", "Jordan"), // Country Code
            new CountryCallingCode(963, "SY", "Syria"), // Country Code
            new CountryCallingCode(964, "IQ", "Iraq"), // Country Code
            new CountryCallingCode(965, "KW", "Kuwait"), // Country Code
            new CountryCallingCode(966, "SA", "Saudi Arabia"), // Country Code
            new CountryCallingCode(967, "YE", "Yemen"), // Country Code
            new CountryCallingCode(968, "OM", "Oman"), // Country Code
            new CountryCallingCode(970, "PS", "Palestine"), // Country Code
            new CountryCallingCode(971, "AE", "United Arab Emirates"), // Country Code
            new CountryCallingCode(972, "IL", "Israel"), // Country Code
            new CountryCallingCode(973, "BH", "Bahrain"), // Country Code
            new CountryCallingCode(974, "QA", "Qatar"), // Country Code
            new CountryCallingCode(975, "BT", "Bhutan"), // Country Code
            new CountryCallingCode(976, "MN", "Mongolia"), // Country Code
            new CountryCallingCode(977, "NP", "Nepal"), // Country Code
            new CountryCallingCode(992, "TJ", "Tajikistan"), // Country Code
            new CountryCallingCode(993, "TM", "Turkmenistan"), // Country Code
            new CountryCallingCode(994, "AZ", "Azerbaijan"), // Country Code
            new CountryCallingCode(995, "GE", "Georgia"), // Country Code
            new CountryCallingCode(996, "KG", "Kyrgyzstan"), // Country Code
            new CountryCallingCode(998, "UZ", "Uzbekistan") // Country Code
    };
}
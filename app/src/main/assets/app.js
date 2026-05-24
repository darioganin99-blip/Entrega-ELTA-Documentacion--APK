
const $ = id => document.getElementById(id);
const LS = { user:"elta_user", last:"elta_last_record" };
const DESTINOS_EMBEBIDOS = [{"name":"AD SAnTO TOME","code":"","calle":"","address":"","locality":"","province":"","lat":0.0,"lng":0.0},{"name":"ALBEnS S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.680751,"lng":-58.341968},{"name":"ALIZZE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.617527,"lng":-58.395988},{"name":"AnCASTI S.R.L.","code":"","calle":"","address":"","locality":"","province":"","lat":-28.466762,"lng":-65.775729},{"name":"AnDInA FRAnCE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-29.399713,"lng":-66.819262},{"name":"AUTOCIEL S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-24.216622,"lng":-65.279633},{"name":"AUTOFRAnCE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.450214,"lng":-58.544094},{"name":"AUTOMOBILES LYOn S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.639733,"lng":-58.533955},{"name":"AUTOMOTORES J. PESADO CASTRO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.921991,"lng":-60.677175},{"name":"AUTOROUTE S.A. (CITROnoRTE)","code":"","calle":"","address":"","locality":"","province":"","lat":-34.534246,"lng":-58.509439},{"name":"AUTOSUR S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-33.052684,"lng":-61.158119},{"name":"AVAnT S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.413839,"lng":-64.269097},{"name":"AVEnUE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.96965,"lng":-60.642462},{"name":"AVEnUE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.396226,"lng":-64.24302},{"name":"AVIGnon AUTO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.658249,"lng":-58.661211},{"name":"CHATELL S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.636025,"lng":-58.791623},{"name":"CI DAnE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.502479,"lng":-58.537437},{"name":"DAMVILLE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.647613,"lng":-58.606378},{"name":"D'ARC LIBERTADOR S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.616773,"lng":-58.52142},{"name":"DRAGO Y BERETTA Y CIA S.A.C.IF","code":"","calle":"","address":"","locality":"","province":"","lat":-34.63779,"lng":-58.502495},{"name":"DS STORE QUILMES (STOCK Vn)","code":"","calle":"","address":"","locality":"","province":"","lat":-34.71492,"lng":-58.266623},{"name":"DUMMY ARCAD","code":"","calle":"","address":"","locality":"","province":"","lat":0.0,"lng":0.0},{"name":"ELYSEES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.646625,"lng":-58.497024},{"name":"ESPRIT POURTAU S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-35.159,"lng":-58.215983},{"name":"ESSOR SRL","code":"","calle":"","address":"","locality":"","province":"","lat":-28.469076,"lng":-65.771882},{"name":"EURODYCAR S.R.L.","code":"","calle":"","address":"","locality":"","province":"","lat":-24.825508,"lng":-65.429175},{"name":"FORTUnATO FORTIno Y CIA S.R.L.","code":"","calle":"","address":"","locality":"","province":"","lat":-26.828296,"lng":-65.21845},{"name":"GALIA S A","code":"","calle":"","address":"","locality":"","province":"","lat":-34.916108,"lng":-57.952415},{"name":"GAULOIS AUTOMOTORES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.457059,"lng":-58.715251},{"name":"GERLI AUTOMOTORES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.110961,"lng":-59.020583},{"name":"GIAMA DEVOTO (ELYSEES S.A.)","code":"","calle":"","address":"","locality":"","province":"","lat":-34.611736,"lng":-58.521102},{"name":"GM CARS S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.431098,"lng":-58.930682},{"name":"GRAn BEnoIT S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.552339,"lng":-68.509719},{"name":"InDIAnA S.A.C.I.F.I.","code":"","calle":"","address":"","locality":"","province":"","lat":-26.808681,"lng":-65.238181},{"name":"IQSA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.923198,"lng":-60.722975},{"name":"IQSA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.396356,"lng":-64.242661},{"name":"LA COnCORDE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.377939,"lng":-55.906673},{"name":"LE MAnS AUTOMOTORES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.807689,"lng":-64.275448},{"name":"LE MAnS AUTOMOTORES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.718769,"lng":-58.263782},{"name":"LE MERIDIEn S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.575393,"lng":-58.548327},{"name":"LE PARC S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.426597,"lng":-63.19921},{"name":"LE ROCHER S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-26.189341,"lng":-58.185998},{"name":"L'EXPRES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.619041,"lng":-58.423266},{"name":"LIBERTADOR AUTOMOVILES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.493439,"lng":-58.49836},{"name":"MARSEILLE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.960524,"lng":-60.646107},{"name":"METZ S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.412817,"lng":-58.958014},{"name":"MIGUEL ROBAYnA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.111132,"lng":-59.020667},{"name":"MOnTER S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.358573,"lng":-64.203622},{"name":"nATIOn LITORAL S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.94344,"lng":-60.643205},{"name":"nAVE MOTORS (STOCK Vn)","code":"","calle":"","address":"","locality":"","province":"","lat":-34.718707,"lng":-58.263813},{"name":"noRT FRAnCE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.467252,"lng":-58.534377},{"name":"noRTHSTELLA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.445682,"lng":-58.886719},{"name":"PARIS AUTOS S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-33.296061,"lng":-66.29803},{"name":"PARRA AUTOMOTORES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.352917,"lng":-64.198217},{"name":"PI InGEnIERIA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.903135,"lng":-57.979061},{"name":"PLAn DE AHORRO AnDInA FRAnCE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-29.399713,"lng":-66.819262},{"name":"ROUGE AUTOMOBILES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.442364,"lng":-58.996429},{"name":"SAnTIAGO GIODA AUTOMOTORES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-33.124703,"lng":-64.384439},{"name":"SEEWALD Y CIA. S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.385236,"lng":-55.90004},{"name":"SVA SACIFI","code":"","calle":"","address":"","locality":"","province":"","lat":-34.599436,"lng":-58.428114},{"name":"SVA SACIFI","code":"","calle":"","address":"","locality":"","province":"","lat":-27.385232,"lng":-55.900045},{"name":"TOULOn S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.628925,"lng":-58.37218},{"name":"TOURS AUTOMOTORES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.439203,"lng":-58.833067},{"name":"VERSALLES AUTOMOTORES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.783082,"lng":-64.26774},{"name":"VESUBIO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-33.208344,"lng":-62.602509},{"name":"4STRADE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.560377,"lng":-58.470801},{"name":"A. RUSSOnIELLO S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-34.5023,"lng":-58.517397},{"name":"ABInGTOn S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.717167,"lng":-58.264951},{"name":"ACAnTILADOS S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.640777,"lng":-60.701402},{"name":"AEROPUERTO DE SALTA","code":"","calle":"","address":"","locality":"","province":"","lat":-24.8437,"lng":-65.478384},{"name":"ALCO ROSARIO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.935908,"lng":-60.696827},{"name":"ALFA MOnDO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.630896,"lng":-58.683114},{"name":"ASP BLInDAJES","code":"","calle":"","address":"","locality":"","province":"","lat":-34.502172,"lng":-58.548257},{"name":"AUTO DAnTE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.680717,"lng":-58.341989},{"name":"AUTO DEL SOL S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.467252,"lng":-58.534377},{"name":"AUTO ROAD S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-28.460887,"lng":-65.764887},{"name":"AUTOMEKAnIKA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-29.428342,"lng":-66.860378},{"name":"AUTOS ZAnET S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.755318,"lng":-58.401503},{"name":"Auto Generali","code":"","calle":"","address":"","locality":"","province":"","lat":-34.575175,"lng":-58.478389},{"name":"AZZURRA AUTO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.169973,"lng":-64.134791},{"name":"BETA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.970392,"lng":-60.629557},{"name":"BROOK MOTORS S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-26.831633,"lng":-65.179545},{"name":"BROOK MOTORS S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-24.789772,"lng":-65.361431},{"name":"BUZZ nET S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.652638,"lng":-58.50347},{"name":"CAR GROUP S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.612035,"lng":-58.399813},{"name":"CHESTER S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.716985,"lng":-58.265075},{"name":"DALLAS MOTORS S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.431249,"lng":-58.930968},{"name":"DETROIT 1925 S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.622594,"lng":-58.389656},{"name":"FADUA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-24.823472,"lng":-65.428026},{"name":"FADUA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-24.19228,"lng":-65.292566},{"name":"GUARnIEL S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.082322,"lng":-59.055494},{"name":"GUInI S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.638439,"lng":-58.380357},{"name":"GUInI SERVICIOS.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.634724,"lng":-58.374157},{"name":"ITAL ROUEn S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.110786,"lng":-59.020643},{"name":"ITALA S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-31.554556,"lng":-68.52687},{"name":"JACK CARS S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.677518,"lng":-58.345359},{"name":"JEA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.418496,"lng":-62.094875},{"name":"LAnDCAR S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.358551,"lng":-64.2036},{"name":"LEDIAn S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-28.460648,"lng":-65.764636},{"name":"LORWEST C.I. S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.558691,"lng":-68.512331},{"name":"MAPIC S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-26.186617,"lng":-58.173942},{"name":"MARCHEnA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.393126,"lng":-55.908762},{"name":"MARInARO E HIJO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.688144,"lng":-58.387775},{"name":"MEnDEZ AUTOMOTORES S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-33.459563,"lng":-61.488846},{"name":"MEnDEZ AUTOMOTORES S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-33.282245,"lng":-62.182404},{"name":"MOEnA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.696818,"lng":-58.354559},{"name":"MOTCOR S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.411852,"lng":-64.269122},{"name":"MOTORVILLE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.445614,"lng":-58.886156},{"name":"MOZZATESI AUTOMOTOR","code":"","calle":"","address":"","locality":"","province":"","lat":-32.935007,"lng":-60.698785},{"name":"nATIOn MOTORS S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.693892,"lng":-60.796449},{"name":"noRTHVILLE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.520938,"lng":-58.473994},{"name":"noVARA AUTOMOTORES S","code":"","calle":"","address":"","locality":"","province":"","lat":-27.800144,"lng":-64.252717},{"name":"NOVO AUTO S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-34.596793,"lng":-60.954665},{"name":"PAnAMER S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.450096,"lng":-58.532816},{"name":"PIAZZA S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-26.831709,"lng":-65.246856},{"name":"PILARITALYAUTO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.445616,"lng":-58.886166},{"name":"PInEROLO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.35854,"lng":-64.203592},{"name":"RUBIC S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.412092,"lng":-64.269103},{"name":"SAn MARCO AUT. SA","code":"","calle":"","address":"","locality":"","province":"","lat":-27.432914,"lng":-59.008838},{"name":"SEEWALD Y CIA. S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.39347,"lng":-55.90885},{"name":"SERGIO TREPAT AUTOMOVILES","code":"","calle":"","address":"","locality":"","province":"","lat":-34.530067,"lng":-58.469928},{"name":"SIDWAY S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.587448,"lng":-58.440473},{"name":"TARABORELLI AUTOMOBILE","code":"","calle":"","address":"","locality":"","province":"","lat":-34.626201,"lng":-58.452154},{"name":"TARABORELLI AUTOMOBILE","code":"","calle":"","address":"","locality":"","province":"","lat":-27.506542,"lng":-64.932229},{"name":"TURIn S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-31.3908,"lng":-64.211082},{"name":"VEnEZIA AUTO S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-34.489918,"lng":-58.623675},{"name":"VEROnA AUTOMOVILES S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-34.68807,"lng":-58.338736},{"name":"VESPASIAnI AUTOMOTORES","code":"","calle":"","address":"","locality":"","province":"","lat":-31.395514,"lng":-64.234604},{"name":"VOLAnT S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-32.970255,"lng":-60.628944},{"name":"Andina Automotores S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-29.437427,"lng":-66.843882},{"name":"Autovia S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-28.471317,"lng":-65.800878},{"name":"FÜRTH AUTOMOTORES S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-27.842496,"lng":-64.243897},{"name":"LOWE S.A.","code":"","calle":"","address":"","locality":"","province":"","lat":-26.404458,"lng":-54.618858},{"name":"Iruña","code":"","calle":"","address":"","locality":"","province":"","lat":-38.961875,"lng":-68.106479},{"name":"BAHIAUT","code":"","calle":"","address":"","locality":"","province":"","lat":-38.687221,"lng":-62.229229},{"name":"Arias Hnos S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-38.733676,"lng":-62.27505},{"name":"DEL SUR EXCLUSIVOS S.A","code":"","calle":"","address":"","locality":"","province":"","lat":-38.680853,"lng":-62.222479},{"name":"Adiclz - BAIC Long","code":"","calle":"BAIC Long","address":"BAIC Long","locality":"","province":"","lat":-34.592774,"lng":-58.50782}];

let step = 1;
let destinos = Array.isArray(DESTINOS_EMBEBIDOS) ? DESTINOS_EMBEBIDOS.slice() : [];
let state = {
  gps:null,
  destino:null,
  lote:"",
  vins:[],
  obs:"Sin observaciones",
  documento:null,
  documentos:[]
};
let currentScanStream = null;
let scanTarget = "";
let barcodeDetector = null;
let scanLoopActive = false;

function load(k,f){ try{return JSON.parse(localStorage.getItem(k)) ?? f}catch(e){return f} }
function save(k,v){ localStorage.setItem(k, JSON.stringify(v)); }
function user(){ return load(LS.user,{fleet:"",driver:"",phone:""}); }
function fmtDate(d){ return new Date(d).toLocaleString("es-AR"); }
function regId(){
  const d=new Date();
  return "ELTA-"+d.getFullYear()+String(d.getMonth()+1).padStart(2,"0")+String(d.getDate()).padStart(2,"0")+"-"+String(d.getHours()).padStart(2,"0")+String(d.getMinutes()).padStart(2,"0")+String(d.getSeconds()).padStart(2,"0");
}

fetch("destinos.json")
  .then(r=>r.json())
  .then(j=>{ destinos = Array.isArray(j) && j.length ? j : DESTINOS_EMBEBIDOS.slice(); renderStep(); })
  .catch(()=>{ destinos = DESTINOS_EMBEBIDOS.slice(); renderStep(); });

function show(id){
  ["entrega","usuario","ultimo"].forEach(s=>{
    const el=$(s);
    if(el) el.classList.toggle("hidden",s!==id);
  });
  ["btn-entrega","btn-usuario","btn-ultimo"].forEach(b=>{
    const el=$(b);
    if(el) el.classList.remove("active");
  });
  const btn=$("btn-"+id);
  if(btn) btn.classList.add("active");
  if(id==="usuario") loadUserForm();
  if(id==="ultimo") renderLast();
  if(id==="entrega") renderStep();
}

function setProgress(){
  const pill=$("stepPillText");
  if(pill) pill.innerText = "Paso " + step;
  for(let i=2;i<=6;i++){
    const el=$("p"+i);
    if(el) el.classList.toggle("on", step>=i);
  }
}

function goStep(n){
  step = Math.max(1, Math.min(6, Number(n)||1));
  show("entrega");
  renderStep();
}

function previousStep(){
  if(step>1) goStep(step-1);
}

function nextStep(){
  if(step<6) goStep(step+1);
}

function renderStep(){
  setProgress();
  const u = user();
  const container = $("stepContent");
  if(!container) return;

  if(step===1){
    $("stepTitle").innerText="Confirmar Entrega";
    container.innerHTML=`
      <div class="unifiedUserBox">
        <b>${(u.fleet || "Sin flota") + " " + (u.driver || "Sin chofer")}</b>
      </div>
      <p class="stepHint">Validar el usuario guardado antes de registrar la entrega.</p>
      <div class="stepActions single">
        <button class="btn" onclick="validateUserAndNext()">Validar datos</button>
      </div>
      <button class="btn light" onclick="show('usuario')">Editar usuario</button>`;
    return;
  }

  if(step===2){
    const gps = state.gps;
    const list = nearbyOptions();
    const opts = list.map((d,i)=>`<option value="${i}">${d.name} · ${Number(d.km || 0).toFixed(2)} km</option>`).join("");
    $("stepTitle").innerText="Destino GPS · Fecha y Hora";
    container.innerHTML=`
      <p class="stepHint">Al validar GPS se calcula automáticamente el destino de entrega usando las coordenadas cargadas.</p>
      <label>Fecha y hora</label>
      <input readonly value="${gps ? fmtDate(gps.time) : ""}" placeholder="Se registra al validar GPS">
      <label>GPS</label>
      <input readonly value="${gps ? gps.lat.toFixed(6)+', '+gps.lng.toFixed(6) : ""}" placeholder="Sin GPS">
      <button class="btn secondary" onclick="getGps()">Validar ubicación con GPS</button>
      ${renderDestinoEntrega()}
      <label>Cambiar destino de entrega</label>
      <select id="destSelect">${opts || '<option value="">Validá GPS para ver destinos cercanos</option>'}</select>
      <p class="debugDest">Destinos cargados: ${destinos.length}. Se muestran destinos dentro de 5 km; si no hay, los 10 más cercanos.</p>
      <button class="btn light" onclick="selectDestino()">Usar destino seleccionado</button>
      <div class="stepActions">
        <button class="btn back" onclick="previousStep()">Volver</button>
        <button class="btn" onclick="nextFromGps()">Continuar</button>
      </div>`;
    return;
  }

  if(step===3){
    $("stepTitle").innerText="Ingresar Número de Lote/Carga";
    container.innerHTML=`
      <label>Lote / Carga</label>
      <input id="lote" inputmode="numeric" value="${state.lote || ""}" placeholder="Ej: 1223">
      <button class="btn secondary" onclick="startScanner('lote')">Escanear código de barras</button>
      <div id="scannerBox" class="hidden"><video id="video" muted playsinline></video><button class="btn light" onclick="stopScanner()">Cerrar cámara</button></div>
      <div class="stepActions">
        <button class="btn back" onclick="previousStep()">Volver</button>
        <button class="btn" onclick="saveLote()">Continuar</button>
      </div>`;
    return;
  }

  if(step===4){
    $("stepTitle").innerText="Ingresar Unidades";
    container.innerHTML=`
      <p class="stepHint">Este paso es opcional. Podés continuar solo con lote o cargar uno o más VIN.</p>
      <label>VIN</label>
      <input id="vin" placeholder="Ingresar VIN o escanear">
      <button class="btn secondary" onclick="startScanner('vin')">Escanear VIN</button>
      <div id="scannerBox" class="hidden"><video id="video" muted playsinline></video><button class="btn light" onclick="stopScanner()">Cerrar cámara</button></div>
      <button class="btn light" onclick="addVin()">Agregar VIN</button>
      <h3>VIN cargados: ${state.vins.length}</h3>
      <div>${state.vins.map((v,i)=>`<span class="chip">${v}<button onclick="removeVin(${i})">×</button></span>`).join("") || '<p class="small">Sin VIN cargados.</p>'}</div>
      <label>Observación general</label>
      <textarea id="obs">${state.obs || "Sin observaciones"}</textarea>
      <div class="stepActions">
        <button class="btn back" onclick="previousStep()">Volver</button>
        <button class="btn" onclick="saveUnits()">Continuar</button>
      </div>`;
    return;
  }

  if(step===5){
    $("stepTitle").innerText="Adjuntar Documentación";
    container.innerHTML=`
      <p class="stepHint">Seleccionar uno o más documentos. Este paso es opcional.</p>
      <div class="docBox">
        <button class="btn secondary" onclick="abrirEscanerDocumentoNativo()">Escanear documento PDF</button>\n        <input class="fileInput" id="docFile" type="file" accept="image/*,application/pdf,.pdf" multiple onchange="handleDocumentos(event)">
        <div id="docName" class="docName">${state.documentos.length ? state.documentos.length + " documento(s) seleccionado(s)" : "Sin documento adjunto"}</div>
        <div id="docList" class="docList">${state.documentos.map((d,i)=>`<div class="docItem">${i+1}. ${d.name}</div>`).join("")}</div>
        <div class="docNote">Tocá “Escanear documento PDF” para usar una app externa de escaneo y guardar el PDF. Luego se adjunta al envío de WhatsApp.</div>
      </div>
      <div class="stepActions">
        <button class="btn back" onclick="previousStep()">Volver</button>
        <button class="btn" onclick="nextStep()">Continuar</button>
      </div>`;
    return;
  }

  if(step===6){
    $("stepTitle").innerText="Enviar";
    const msg = buildMessage(false);
    container.innerHTML=`
      <div class="summary">${escapeHtml(msg)}</div>
      ${state.documentos.length ? `<p class="small ok">${state.documentos.length} documento(s) seleccionado(s).</p>` : `<p class="small">Sin documento adjunto.</p>`}
      <button class="btn" onclick="sendWhatsapp()">Enviar por WhatsApp</button>
      <div class="stepActions">
        <button class="btn back" onclick="previousStep()">Volver</button>
        <button class="btn light" onclick="goStep(1)">Inicio</button>
      </div>`;
    return;
  }
}

function validateUserAndNext(){
  const u=user();
  if(!u.fleet){
    alert("Completá la Flota en Usuario.");
    show("usuario");
    return;
  }
  goStep(2);
}

function getGps(){
  if(!navigator.geolocation){ alert("GPS no disponible."); return; }
  navigator.geolocation.getCurrentPosition(pos=>{
    state.gps={lat:pos.coords.latitude,lng:pos.coords.longitude,acc:pos.coords.accuracy || 0,time:new Date().toISOString()};
    const nearest = nearestDestino(state.gps.lat,state.gps.lng);
    if(nearest) state.destino = nearest;
    else alert("No hay destinos cargados para calcular cercanía.");
    renderStep();
  }, err=>{
      let msg = err.message || "Error desconocido";
      if(err.code === 1) msg = "Permiso de ubicación denegado. Activá Ubicación para la app.";
      if(err.code === 2) msg = "Ubicación no disponible. Activá GPS y datos móviles.";
      if(err.code === 3) msg = "Tiempo agotado obteniendo GPS. Intentá nuevamente.";
      alert("No se pudo obtener GPS: " + msg);
    }, {enableHighAccuracy:true,timeout:20000,maximumAge:0});
}

function distKm(a,b,c,d){
  const R=6371, toRad=x=>x*Math.PI/180;
  const dLat=toRad(c-a), dLng=toRad(d-b);
  const s=Math.sin(dLat/2)**2 + Math.cos(toRad(a))*Math.cos(toRad(c))*Math.sin(dLng/2)**2;
  return R*2*Math.atan2(Math.sqrt(s),Math.sqrt(1-s));
}

function cleanDestinos(){
  return (destinos && destinos.length ? destinos : DESTINOS_EMBEBIDOS)
    .map(d => ({...d, lat:Number(d.lat), lng:Number(d.lng)}))
    .filter(d => Number.isFinite(d.lat) && Number.isFinite(d.lng));
}

function nearestDestino(lat,lng){
  const clean = cleanDestinos();
  if(!clean.length) return null;
  return clean.map(d=>({...d,km:distKm(lat,lng,d.lat,d.lng)})).sort((a,b)=>a.km-b.km)[0];
}

function nearbyOptions(){
  if(!state.gps) return [];
  const all = cleanDestinos().map(d=>({...d,km:distKm(state.gps.lat,state.gps.lng,d.lat,d.lng)})).sort((a,b)=>a.km-b.km);
  const within5 = all.filter(d=>d.km<=5);
  return within5.length ? within5 : all.slice(0,10);
}

function selectDestino(){
  const idx = Number($("destSelect").value);
  const list = nearbyOptions();
  if(Number.isFinite(idx) && list[idx]) state.destino = list[idx];
  renderStep();
}

function nextFromGps(){
  if(!state.gps){ alert("Primero validá GPS."); return; }
  if(!state.destino){ alert("Seleccioná un destino."); return; }
  goStep(3);
}

function saveLote(){
  const input=$("lote");
  const val=input ? input.value.trim() : "";
  if(!val){ alert("Ingresá el número de lote/carga."); return; }
  state.lote=val;
  goStep(4);
}

function addVin(){
  const input=$("vin");
  const v=input ? input.value.trim().toUpperCase() : "";
  if(!v) return;
  if(!state.vins.includes(v)) state.vins.push(v);
  renderStep();
}

function removeVin(i){ state.vins.splice(i,1); renderStep(); }

function saveUnits(){
  const obs=$("obs");
  state.obs = obs && obs.value.trim() ? obs.value.trim() : "Sin observaciones";
  goStep(5);
}

function cleanText(v){
  const s=String(v || "").trim();
  if(!s || s.toLowerCase()==="null" || s.toLowerCase()==="undefined") return "";
  return s;
}

function destinoDireccionCompleta(destino){
  if(!destino) return "";
  const address=cleanText(destino.address);
  const calle=cleanText(destino.calle);
  if(address) return address;
  if(calle) return calle;
  return "";
}

function destinoCodigo(nombre){
  const m = String(nombre || "").match(/(?:Código|Codigo|Cod|Cód)[:\s.-]*([A-Za-z0-9-]+)/i);
  return m ? m[1] : "";
}

function destinoZona(nombre){
  const parts = String(nombre || "").split(/\s+-\s+|\s+·\s+/).map(x=>x.trim()).filter(Boolean);
  if(parts.length >= 2) return parts.slice(1).join(" · ");
  return "";
}

function destinoNombrePrincipal(nombre){
  const parts = String(nombre || "").split(/\s+-\s+|\s+·\s+/).map(x=>x.trim()).filter(Boolean);
  return parts[0] || String(nombre || "");
}

function renderDestinoEntrega(){
  if(!state.destino) return "";
  const nombre = destinoNombrePrincipal(state.destino.name);
  const direccion = destinoDireccionCompleta(state.destino);
  const zona = destinoZona(state.destino.name);
  const codigo = state.destino.code || destinoCodigo(state.destino.name);
  const codigoTxt = codigo ? `Código: ${codigo}` : "";
  const zonaTxt = zona ? `${codigoTxt ? " · " : ""}${zona}` : "";
  const meta = `${codigoTxt}${zonaTxt}`;
  const distancia = state.destino.km != null ? `Distancia aprox.: ${Number(state.destino.km).toFixed(1)} km` : "";
  return `
    <div class="destinoEntregaCard">
      <div class="destLabel">Destino de entrega seleccionado:</div>
      <div class="destName">${nombre}</div>
      ${direccion ? `<div class="destStreet">Dirección: ${direccion}</div>` : ""}
      ${meta ? `<div class="destMeta">${meta}</div>` : ""}
      <div class="destMeta">${distancia}</div>
    </div>`;
}


function abrirEscanerDocumentoNativo(){
  if(window.AndroidBridge && AndroidBridge.openDocumentScanner){
    AndroidBridge.openDocumentScanner();
  }else{
    const input = $("docFile");
    if(input) input.click();
    else alert("Selector de documentos no disponible.");
  }
}

window.onNativeDocumentSelected = function(names){
  const list = String(names || "").split(",").filter(Boolean);
  if(!state.documentos) state.documentos = [];
  list.forEach(n => state.documentos.push({name:n, type:"application/pdf", size:0}));
  state.documento = state.documentos[0] || null;
  renderStep();
};

function handleDocumentos(event){
  const files = Array.from((event.target && event.target.files) ? event.target.files : []);
  state.documentos = files.map((file, index) => ({name:file.name || ("documento_" + (index + 1)), type:file.type || "", size:file.size || 0}));
  state.documento = state.documentos[0] || null;
  renderStep();
}

function buildMessage(finalize){
  const u=user(), gps=state.gps, id=regId();
  const direccionDestino = state.destino ? destinoDireccionCompleta(state.destino) : "";
  const unidades = state.vins.length ? "VIN informados:\n"+state.vins.map(v=>"- "+v).join("\n") : "Informado solo con lote.";
  const documentosTxt = state.documentos && state.documentos.length ? "Documentos: " + state.documentos.map(d=>d.name).join(", ") + "\n" : "";
  const msg = `ELTA - Registro de Entrega de unidades
N° registro: ${id}
Chofer: ${u.driver}
Flota: ${u.fleet}
Fecha entrega: ${gps ? fmtDate(gps.time) : fmtDate(new Date())}
Lote: ${state.lote}
Destino: ${state.destino ? state.destino.name : ""}
${direccionDestino ? "Dirección destino: " + direccionDestino + "\n" : ""}GPS: ${gps ? gps.lat.toFixed(6)+", "+gps.lng.toFixed(6) : ""}
${unidades}
${documentosTxt}Observación general: ${state.obs || "Sin observaciones"}`;
  if(finalize) save(LS.last,{msg, date:new Date().toISOString()});
  return msg;
}

function sendWhatsapp(){
  const u=user();
  if(!u.phone){ alert("Cargá el teléfono de WhatsApp en Usuario."); show("usuario"); return; }
  const msg=buildMessage(true);

  if(window.AndroidBridge && AndroidBridge.sendWhatsappWithDocuments){
    AndroidBridge.sendWhatsappWithDocuments(u.phone, msg);
  }else{
    const url=`whatsapp://send?phone=${u.phone}&text=${encodeURIComponent(msg)}`;
    window.location.href=url;
  }

  state={gps:null,destino:null,lote:"",vins:[],obs:"Sin observaciones",documento:null,documentos:[]};
  step=1;
  setTimeout(renderStep,800);
}

function resendLast(){
  const last=load(LS.last,null), u=user();
  if(!last){ alert("No hay último registro."); return; }
  if(!u.phone){ alert("Cargá el teléfono en Usuario."); show("usuario"); return; }
  window.location.href=`https://wa.me/${u.phone}?text=${encodeURIComponent(last.msg)}`;
}

function renderLast(){
  const last=load(LS.last,null);
  const box=$("lastBox");
  if(box) box.innerText = last ? last.msg : "Todavía no hay registros enviados.";
}

function loadUserForm(){
  const u=user();
  if($("userFleet")) $("userFleet").value=u.fleet||"";
  if($("userDriver")) $("userDriver").value=u.driver||"";
  if($("userPhone")) $("userPhone").value=u.phone||"";
}

function saveUser(){
  save(LS.user,{fleet:$("userFleet").value.trim(),driver:$("userDriver").value.trim(),phone:$("userPhone").value.trim().replace(/\D/g,"")});
  if($("userMsg")) $("userMsg").innerHTML='<span class="ok">Usuario guardado correctamente.</span>';
}

async function startScanner(target){
  scanTarget = target;
  const box = $("scannerBox");
  const video = $("video");
  if(!box || !video){ alert("No se encontró el visor de cámara."); return; }
  if(!navigator.mediaDevices || !navigator.mediaDevices.getUserMedia){ alert("Cámara no disponible en este dispositivo."); return; }
  box.classList.remove("hidden");
  try{
    currentScanStream = await navigator.mediaDevices.getUserMedia({video:{ facingMode:{ ideal:"environment" } }, audio:false});
    video.srcObject = currentScanStream;
    video.setAttribute("playsinline", "true");
    await video.play();
    if("BarcodeDetector" in window){
      barcodeDetector = new BarcodeDetector({formats:["code_128","code_39","code_93","ean_13","ean_8","upc_a","upc_e","itf","codabar","qr_code","data_matrix","pdf417"]});
      scanLoopActive = true;
      scanBarcodeLoop();
    }else{
      alert("La cámara abrió, pero este WebView no soporta lector automático de códigos. Ingresá el dato manualmente.");
    }
  }catch(e){ alert("No se pudo abrir la cámara: " + e.message); }
}

async function scanBarcodeLoop(){
  const video = $("video");
  if(!scanLoopActive || !barcodeDetector || !video) return;
  try{
    if(video.readyState >= 2){
      const codes = await barcodeDetector.detect(video);
      if(codes && codes.length){
        const value = (codes[0].rawValue || "").trim();
        if(value){ applyScannedValue(value); stopScanner(); return; }
      }
    }
  }catch(e){ console.log("Error leyendo código:", e); }
  requestAnimationFrame(scanBarcodeLoop);
}

function applyScannedValue(value){
  if(scanTarget === "lote"){
    const input = $("lote");
    if(input) input.value = value;
    state.lote = value;
  }
  if(scanTarget === "vin"){
    const input = $("vin");
    if(input) input.value = value;
  }
}

function stopScanner(){
  scanLoopActive = false;
  barcodeDetector = null;
  if(currentScanStream){ currentScanStream.getTracks().forEach(t=>t.stop()); currentScanStream = null; }
  const video = $("video");
  if(video){ video.pause(); video.srcObject = null; }
  const box = $("scannerBox");
  if(box) box.classList.add("hidden");
}

function escapeHtml(s){ return String(s).replace(/[&<>"']/g,m=>({"&":"&amp;","<":"&lt;",">":"&gt;","\"":"&quot;","'":"&#039;"}[m])); }

show("entrega");
renderStep();

(self.webpackChunk_N_E=self.webpackChunk_N_E||[]).push([[1785],{87111:function(e,o,t){"use strict";t.d(o,{bK:function(){return useRoleAdminUploadById}});var n=t(82066),r=t(12825);let adminUploadKey=e=>{let{uploadId:o}=e;return[{id:"admin-uploads",scope:"legacy-uploads",entity:"list",uploadId:o}]},fetchAdminUploadById=async e=>{let{queryKey:[{uploadId:o}]}=e,t=await (0,r._i)("/v2/admin/upload/".concat(o));return t.item},useRoleAdminUploadById=e=>{let{enabled:o,...t}=e;return(0,n.useQuery)({queryKey:adminUploadKey(t),queryFn:fetchAdminUploadById,enabled:o&&void 0!==t.uploadId})}},92609:function(e,o,t){"use strict";t.d(o,{j:function(){return useDeleteUpload}});var n=t(31171),r=t(82066),i=t(12825),l=t(15442),s=t(26021),a=t(24304);let deleteUpload=async e=>{let{params:o,query:t}=e,n=t?(0,l.P)({...t,...void 0===t.removeMoney?{}:{removeMoney:t.removeMoney?1:0},...void 0===t.copyright?{}:{copyright:t.copyright?1:0},...void 0===t.closeReport?{}:{closeReport:t.closeReport?1:0}}):"",r=await (0,i._i)("/upload/".concat(o.uploadId).concat(n),{method:"DELETE"});return r},useDeleteUpload=()=>{let e=(0,r.useQueryClient)(),{t:o}=(0,n.qM)();return(0,r.useMutation)(deleteUpload,{onMutate:async o=>{let{extra:{artifactId:t}}=o;(0,a.V)({queryClient:e,artifactId:t})},onError:async t=>{401!==t.status&&((0,s.A)({title:"Error",description:o(t.key),status:"error",duration:5e3}),e.invalidateQueries({predicate:e=>!!e.queryKey&&"artifacts"===e.queryKey[0].id}))},onSuccess:async()=>{(0,s.A)({title:"Archivo borrado",description:"Archivo borrado correctamente",status:"success",duration:5e3}),e.resetQueries([{id:"user-uploads"}])}})}},2313:function(e,o,t){"use strict";t.d(o,{aF:function(){return useUser},sY:function(){return useUserById}});var n=t(82066),r=t(12825),i=t(98665);let userKey=e=>[{id:"user",scope:"user",entity:"detail",...e}],fetchUser=async e=>{let{queryKey:[o]}=e,{nickname:t}=o,n=await (0,r._i)("/v2/users/".concat(t));return(0,i.B)(n)},fetchUserById=async e=>{let{queryKey:[o]}=e,{userId:t}=o,n=await (0,r._i)("/v2/users/".concat(t));return(0,i.B)(n)},useUser=e=>(0,n.useQuery)({queryKey:userKey(e),queryFn:fetchUser,enabled:!!e.nickname}),useUserById=e=>(0,n.useQuery)({queryKey:userKey(e),queryFn:fetchUserById,enabled:!!e.userId})},9514:function(e,o,t){"use strict";var n=t(52322),r=t(4036);o.Z=e=>{let{...o}=e;return(0,n.jsx)(r.C,{bg:"#FFE900",fontSize:"8px",h:"12px",px:"4px",display:"flex",alignItems:"center",justifyContent:"center",fontWeight:"400",borderRadius:"full",textTransform:"uppercase",color:"#d83d00",...o,children:"Adm"})}},86393:function(e,o,t){"use strict";t.d(o,{qc:function(){return AdminDeleteButton},eC:function(){return AdminDeleteMenuItem},CQ:function(){return AdminDeleteUploadMenuItem}});var n=t(52322),r=t(44354),i=t(96012),l=t(69316),s=t(26488),a=t(8283),d=t(91012),u=t(9514),c=t(99022),p=t(31171),m=t(87111),x=t(92609),h=t(60489),f=t(52742);let useAdminDeleteUploadButton=e=>{let{uploadId:o,artifactId:t,artifactValue:r}=e,{t:i}=(0,p.qM)(),{mutateAsync:l}=(0,x.j)(),{data:s}=(0,m.bK)({uploadId:o,enabled:void 0!==o}),a={caption:i("delete"),icon:(0,n.jsx)(f.Bhs,{})};if(void 0===o||void 0===r)return{...a,onClick:()=>{},showSkeleton:!0};let onDelete=e=>{let{values:n}=e;return l({params:{uploadId:o},query:n,extra:{artifactId:t}})};return{...a,onClick:()=>{h.Ln.onOpen({name:null==s?void 0:s.title,type:"upload",onDelete,money:null==s?void 0:s.money})},showSkeleton:!1}},AdminDeleteButton=e=>{let{documentId:o,children:t,...r}=e,{caption:i,icon:p,onClick:m,showSkeleton:x}=(0,c.u)({documentId:o});return(0,n.jsx)(l.z,{leftIcon:p,onClick:m,...x?{as:s.O}:{},...r,children:(0,n.jsxs)(a.U,{children:[(0,n.jsx)(d.x,{children:t||i}),(0,n.jsx)(u.Z,{})]})})},AdminDeleteMenuItem=e=>{let{locationType:o,icon:t,onClick:l,showSkeleton:c,children:p,...m}=e;return(0,n.jsx)(i.s,{icon:t,onClick:e=>{(0,r.O)({locationType:o,location:"artifact expanded",button:"delete"}),l&&l(e)},...c?{as:s.O}:{},...m,"data-testid":"delete-artifact-button-admin",children:(0,n.jsxs)(a.U,{children:[(0,n.jsx)(d.x,{children:p}),(0,n.jsx)(u.Z,{})]})})},AdminDeleteUploadMenuItem=e=>{let{uploadId:o,locationType:t,artifactId:r,artifactValue:i,children:l,...s}=e,{caption:a,icon:d,onClick:u,showSkeleton:c}=useAdminDeleteUploadButton({uploadId:o,artifactId:r,artifactValue:i});return(0,n.jsx)(AdminDeleteMenuItem,{locationType:t,icon:d,onClick:u,showSkeleton:c,...s,children:a})}},38202:function(e,o,t){"use strict";t.d(o,{d:function(){return LegacyDownloadButton},y:function(){return LegacyDownloadIconButton}});var n=t(52322),r=t(98850),i=t(69316),l=t(25951),s=t(26488),a=t(65967);let LegacyDownloadButton=e=>{let{document:o,location:t,children:l,...d}=e,{allowDownload:u,caption:c,icon:p,onClick:m,isLoading:x,showSkeleton:h}=(0,a.S)({document:o});return u?(0,n.jsx)(i.z,{leftIcon:p,onClick:()=>{(0,r.G)({location:t,button:"download"}),m()},isLoading:x,...h?{as:s.O}:{},...d,"data-testid":"legacy-download-button",children:l||c}):null},LegacyDownloadIconButton=e=>{let{document:o,location:t,...i}=e,{allowDownload:d,caption:u,icon:c,onClick:p,isLoading:m,showSkeleton:x}=(0,a.S)({document:o});return d?(0,n.jsx)(l.h,{"aria-label":u,title:u,icon:c,onClick:()=>{(0,r.G)({location:t,button:"download"}),p()},isLoading:m,...x?{as:s.O}:{},...i,"data-testid":"legacy-download-button"}):null}},65967:function(e,o,t){"use strict";t.d(o,{S:function(){return useDownloadButton}});var n=t(52322),r=t(31171),i=t(2784),l=t(22487),s=t(5261),a=t(17395),d=t(60489),u=t(92415),c=t(52742);let useDownloadButton=e=>{let{document:o}=e,{t}=(0,r.qM)(),{data:p}=(0,s.Pi)(),{data:m}=(0,s.WI)(),x=(0,l.D)(),h=(0,a.G)(),f=(0,i.useCallback)(async()=>{if(!o)return;let e=p&&o.userId===p.id;if(e){await x.mutateAsync({document:o});return}let t="pdf"===o.extension.wuolah;if(t){d.aB.onOpen({document:o});return}m?await x.mutateAsync({document:o}):h.show({document:o})},[o,x.mutateAsync,p,m]);return{allowDownload:!!o&&(0,u.H)(o),showSkeleton:!o,onClick:f,caption:t("document-download-button"),icon:(0,n.jsx)(c.H_7,{}),isLoading:x.isLoading}}},43862:function(e,o,t){"use strict";t.d(o,{o:function(){return useQuickDownloadButton}});var n=t(52322),r=t(31171),i=t(2784),l=t(22487),s=t(44926),a=t(5261),d=t(26021),u=t(92415),c=t(52742);let useQuickDownloadButton=e=>{let{document:o}=e,{t}=(0,r.qM)(),{askAuth:p}=(0,s.S)(),{data:m}=(0,a.WI)(),x=(0,l.D)(),h=(0,i.useCallback)(async()=>{if(!o)return;let{isAuth:e}=p();if(e){if(!m){(0,d.A)({title:t("document-quick-download-button-pro-only"),status:"error"});return}await x.mutateAsync({document:o})}},[o,m,x,p,t]);return{allowDownload:!!o&&(0,u.H)(o),showSkeleton:!o,onClick:h,caption:t("document-quick-download-button"),icon:(0,n.jsx)(c.H_7,{}),isLoading:x.isLoading}}},80019:function(e,o,t){"use strict";t.d(o,{Qm:function(){return VoucherDownloadButton},u$:function(){return VoucherDownloadMenuItem}});var n=t(52322),r=t(60489),i=t(69316),l=t(96012),s=t(8283),a=t(91012),d=t(9514),u=t(52742);let VoucherDownloadButton=e=>{let{documentId:o}=e;return o?(0,n.jsx)(i.z,{leftIcon:(0,n.jsx)(u.H_7,{}),onClick:()=>r.UW.onOpen({documentId:o}),children:(0,n.jsxs)(s.U,{children:[(0,n.jsx)(a.x,{children:"Descargar comprobante"}),(0,n.jsx)(d.Z,{})]})}):null},VoucherDownloadMenuItem=e=>{let{documentId:o}=e;return(0,n.jsx)(l.s,{icon:(0,n.jsx)(u.H_7,{}),onClick:()=>r.UW.onOpen({documentId:o}),children:(0,n.jsxs)(s.U,{children:[(0,n.jsx)(a.x,{children:"Descargar comprobante"}),(0,n.jsx)(d.Z,{})]})})}},25814:function(e,o,t){"use strict";t.d(o,{Is:function(){return SaveIconButton},Mp:function(){return SaveMenuItem},k$:function(){return SaveButton}});var n=t(52322),r=t(98850),i=t(69316),l=t(25951),s=t(96012),a=t(26488),d=t(78981);let SaveButton=e=>{let{document:o,location:t,children:l,...s}=e,{caption:u,icon:c,onClick:p,colorScheme:m,isLoading:x,showSkeleton:h}=(0,d.t)({document:o});return(0,n.jsx)(i.z,{leftIcon:c,onClick:()=>{(0,r.G)({location:t,button:"save"}),p()},colorScheme:m,isLoading:x,...h?{as:a.O}:{},...s,"data-testid":"save-document",children:l||u})},SaveIconButton=e=>{let{document:o,location:t,...i}=e,{caption:s,icon:u,onClick:c,colorScheme:p,isLoading:m,showSkeleton:x}=(0,d.t)({document:o});return(0,n.jsx)(l.h,{"aria-label":s,title:s,icon:u,onClick:()=>{(0,r.G)({location:t,button:"save"}),c()},colorScheme:p,isLoading:m,...x?{as:a.O}:{},...i})},SaveMenuItem=e=>{let{document:o,location:t,children:i,...l}=e,{caption:u,icon:c,onClick:p,colorScheme:m,isLoading:x,showSkeleton:h}=(0,d.t)({document:o});return(0,n.jsx)(s.s,{icon:c,onClick:e=>{e.stopPropagation(),(0,r.G)({location:t,button:"save"}),p()},colorScheme:m,isLoading:x,...h?{as:a.O}:{},...l,children:i||u})}},98317:function(e,o,t){"use strict";t.d(o,{y:function(){return useShareButton}});var n=t(52322),r=t(31171),i=t(28035),l=t(5261),s=t(77904),a=t(14360),d=t(57851),u=t(34543);let useShareButton=e=>{var o,t,c;let{document:p}=e,{t:m}=(0,r.qM)(),x=(0,l.Lw)(),{data:h}=(0,l.Pi)(),{shouldSeeSandbox:f}=(0,s.U)(),w=!(null==p?void 0:p.community)||!(null==p?void 0:p.subject),j=(0,i.UQ)({communityId:w?null==p?void 0:p.communityId:void 0,subjectId:w?null==p?void 0:p.subjectId:void 0}),v=(null==p?void 0:null===(o=p.subject)||void 0===o?void 0:o.slug)||(null==j?void 0:null===(c=j.data)||void 0===c?void 0:null===(t=c.subject)||void 0===t?void 0:t.slug),y={utm_source:"wuolah",utm_medium:"referral",utm_campaign:"file-sharefile",...x&&{referral:h&&h.invitationCode}},g=(()=>{let e="".concat("https://wuolah.com").concat((0,a.R)({subjectSlug:v,documentSlug:null==p?void 0:p.slug,shouldSeeSandbox:f})),o=new URL(e),t=new URLSearchParams(y);return t.forEach((e,t)=>{o.searchParams.set(t,e)}),o.toString()})(),{onShare:D}=(0,d.S)({url:g});return{showSkeleton:!p,onClick:D,caption:m("share-button"),icon:(0,n.jsx)(u.PMT,{})}}},71938:function(e,o,t){"use strict";t.d(o,{iB:function(){return LegacyDownloadFolderButton},U8:function(){return LegacyDownloadFolderIconButton}});var n=t(52322),r=t(75191),i=t(69316),l=t(25951),s=t(8283),a=t(91012),d=t(53738),u=t(26488),c=t(49454),p=t(43638);let FolderDownloadIcon=e=>{let{size:o="28px",...t}=e;return(0,n.jsxs)(p.J,{name:"folder",w:o,h:o,viewBox:"0 0 16 16",...t,children:[(0,n.jsxs)("g",{clipPath:"url(#a)",children:[(0,n.jsx)("path",{fill:"#FECE5C",d:"M14.0644 2.87742c.2171-.01057.4345.01117.6452.06452.3236.08388.6156.26044.8403.508.2246.24756.372.55534.4242.88554.0211.14956.0297.30062.0258.45162v7.7677c-.0028.4827-.113.9588-.3226 1.3936-.3507.7573-.9589 1.3654-1.7161 1.7161-.3444.1673-.7158.2721-1.0968.3097L12.4515 16H3.38054c-.40882-.0071-.81221-.0948-1.1871-.2581-.52518-.2078-.99147-.541-1.358103-.9707-.366638-.4296-.622466-.9425-.7451222-1.4938-.0617317-.227-.09212083-.4615-.09032253-.6968V3.38065c.00539449-.40353.08863793-.80222.24516173-1.1742.134462-.33716.321753-.65077.554838-.92903C1.13706.868139 1.5599.537796 2.0386.309677c.30754-.127571.62756-.2227097.95484-.2838705L3.41925 0h2.8258c.68387 0 1.1742.322581 1.52258.890323.04824.087743.09132.178217.12904.270967.0129.02581.0129.03871.0258.05161.01037.02443.01479.05096.01291.07742.04665.1503.06844.3072.06451.46452-.0129.33548 0 .67097 0 1.01935 0 .10323 0 .10323.09032.10323h5.97419Z"}),(0,n.jsx)("path",{fill:"#F8B452",d:"M14.0645 2.87741H8.09032C8 2.87741 8 2.87741 8 2.77418c0-.34838-.01291-.68387 0-1.01935.00393-.15732-.01787-.31422-.06452-.46452.00188-.02646-.00254-.05299-.0129-.07742h5.30322c.1886-.00846.3747.04601.529.15484.0991.06316.1798.15123.2342.25539.0543.10417.0804.22077.0755.33816v.91613Z"})]}),(0,n.jsx)("path",{fill:"#EBA23A",fillRule:"evenodd",d:"M5.04456 12.0667c0-.2332.18903-.4222.42222-.4222h5.06662c.2332 0 .4223.189.4223.4222 0 .2332-.1891.4223-.4223.4223H5.46678c-.23319 0-.42222-.1891-.42222-.4223Zm1.39033-3.25408c.16489-.16489.43222-.16489.59711 0l.54589.54589V6.15562c0-.23319.18903-.42222.42222-.42222.23319 0 .42222.18903.42222.42222v3.20289l.54589-.54589c.16489-.16489.43223-.16489.59711 0 .16489.16489.16489.43222 0 .59711L8.29867 10.6764c-.07919.0792-.18658.1237-.29856.1237s-.21937-.0445-.29856-.1237L6.43489 9.40973c-.16489-.16489-.16489-.43222 0-.59711Z",clipRule:"evenodd"}),(0,n.jsx)("defs",{children:(0,n.jsx)("clipPath",{id:"a",children:(0,n.jsx)("path",{fill:"#fff",d:"M0 0h16v16H0z"})})})]})};var m=t(31171),x=t(2784),h=t(20811),f=t(57569),w=t(92415),j=t(52742);let useDownloadButton_useDownloadButton=e=>{let{upload:o}=e,{t}=(0,m.qM)(),{folderDocuments:r=[],isLoading:i}=(0,h.W)({uploadId:null==o?void 0:o.id}),{download:l,isLoading:s}=(0,f.n)(),a=r.filter(e=>(0,w.H)(e)),d=(0,x.useCallback)(async()=>{o&&await l({upload:o,withAds:!1})},[l,o]),u=!o||i,c=u||a.length>0;return{showSkeleton:u,onClick:d,caption:t("folder-download-button"),isLoading:s,icon:(0,n.jsx)(j.H_7,{}),allowDownload:c}},LegacyDownloadFolderButton=e=>{let{upload:o,location:t,children:l,...d}=e,{caption:p,onClick:m,isLoading:x,showSkeleton:h,icon:f,allowDownload:w}=useDownloadButton_useDownloadButton({upload:o});return w?(0,n.jsx)(i.z,{leftIcon:f,onClick:()=>{(0,r.u)({location:t,button:"download"}),m()},isLoading:x,...h?{as:u.O}:{},...d,"data-testid":"folder-download-modal-button",children:(0,n.jsxs)(s.U,{children:[(0,n.jsxs)(a.x,{children:[" ",l||p]}),(0,n.jsx)(c.Z,{size:d.size})]})}):null},LegacyDownloadFolderIconButton=e=>{let{upload:o,location:t,...i}=e,{caption:s,onClick:a,isLoading:p,showSkeleton:m,allowDownload:x}=useDownloadButton_useDownloadButton({upload:o});return x?(0,n.jsxs)(d.xu,{position:"relative",children:[(0,n.jsx)(l.h,{"aria-label":s,title:s,icon:(0,n.jsx)(FolderDownloadIcon,{size:"14px"}),onClick:()=>{(0,r.u)({location:t,button:"download"}),a()},isLoading:p,...m?{as:u.O}:{},...i}),(0,n.jsx)(c.Z,{position:"absolute",top:"-4px",right:"-8px"})]}):null}},43145:function(e,o,t){"use strict";t.d(o,{f:function(){return ReportButton},S:function(){return ReportMenuItem}});var n=t(52322),r=t(74587),i=t(69316),l=t(26488),s=t(31171),a=t(66946),d=t(2784),u=t(44926),c=t(60489),p=t(52742);let useReportButton=e=>{let{variant:o,item:t}=e,{t:r}=(0,s.qM)(),{askAuth:i}=(0,u.S)(),l=(0,d.useCallback)(()=>{if(!t)return;let{isAuth:e}=i();e&&(o===a.t.DOCUMENT&&c.iE.onOpen({document:t}),o===a.t.SOCIAL&&c.gs.onOpen({social:t}),o===a.t.SUBJECT&&c.DX.onOpen({subject:t}),o===a.t.COMMUNITY&&c.vC.onOpen({community:t}))},[o,t,i]);return{showSkeleton:!t,onClick:l,caption:r("report-button"),icon:(0,n.jsx)(p.QNV,{})}},ReportButton=e=>{let{report:o,eventFunction:t,children:r,...s}=e,{caption:a,icon:d,onClick:u,showSkeleton:c}=useReportButton(o);return(0,n.jsx)(i.z,{leftIcon:d,onClick:()=>{t&&t(),u()},...c?{as:l.O}:{},...s,"data-testid":"report-document",children:r||a})},ReportMenuItem=e=>{let{report:o,eventFunction:t}=e,{caption:i,icon:s,onClick:a,showSkeleton:d}=useReportButton(o);return(0,n.jsx)(r.s,{icon:s,label:i,onClick:e=>{e.stopPropagation(),t&&t(),a()},...d?{as:l.O}:{},"data-testid":"report-document"})}},5855:function(e,o,t){"use strict";t.d(o,{Z:function(){return Header_InfoIconButton}});var n=t(52322),r=t(31171),i=t(98850),l=t(25951),s=t(52742),a=t(66946),d=t(2784),u=t(5261),c=t(24642),p=t(31116),m=t(73202),x=t(75944),h=t(5389),f=t(56915),w=t(82962),j=t(64827),v=t(2167),y=t(71354),g=t(40110),D=t(39675),b=t(51434),C=t(91333),k=t(23742),_=t(83897),I=t(30807),B=t(86393),P=t(38202),T=t(69316),M=t(8283),F=t(91012),S=t(26488),L=t(49454),O=t(43862);let LegacyQuickDownloadButton=e=>{let{document:o,location:t,children:r,...l}=e,{allowDownload:s,caption:a,icon:d,onClick:u,isLoading:c,showSkeleton:p}=(0,O.o)({document:o});return s?(0,n.jsx)(T.z,{leftIcon:d,onClick:()=>{(0,i.G)({location:t,button:"quick download"}),u()},isLoading:c,...p?{as:S.O}:{},...l,"data-testid":"quick-download-document",children:(0,n.jsxs)(M.U,{children:[(0,n.jsx)(F.x,{children:r||a}),(0,n.jsx)(L.Z,{size:l.size})]})}):null};var U=t(80019),E=t(25814),A=t(43145),R=t(81114),N=t(1540),q=t(2313),z=t(75787),G=t(89140),K=t(77904),Z=t(60489),H=t(14538),W=t(39831),Q=t(84858),V=t(84103),Y=t(27302),J=t(56467),$=t(65186);let Info=e=>{var o,t,i,l,a;let{document:d,...u}=e,{t:c}=(0,r.qM)(),{data:p}=(0,q.sY)({userId:(null==d?void 0:d.user)?void 0:String(null==d?void 0:d.userId)}),m=(0,R.BY)({id:(null==d?void 0:null===(o=d.community)||void 0===o?void 0:o.center)?void 0:null==d?void 0:d.centerId}),x=(0,N.Os)({studyId:(null==d?void 0:d.study)?void 0:null==d?void 0:d.studyId}),{shouldSeeSandbox:h}=(0,K.U)(),f=null!==(i=null==d?void 0:null===(t=d.community)||void 0===t?void 0:t.center)&&void 0!==i?i:m.data,w=null!==(l=null==d?void 0:d.study)&&void 0!==l?l:x.data,j=null!==(a=null==d?void 0:d.user)&&void 0!==a?a:p;return(0,n.jsxs)(y.K,{spacing:"3",fontSize:"12px",fontWeight:"medium",...u,children:[(null==d?void 0:d.isAnonymous)?(0,n.jsxs)(M.U,{children:[(0,n.jsx)(s.UUf,{size:"24px"}),(0,n.jsx)(F.x,{children:c("user-anonymous").toLocaleLowerCase()})]}):(0,n.jsx)(J.p,{avoidAnchor:!0,href:h?(0,W.U)({userSlug:null==j?void 0:j.nickname}):(0,Q.d)({userSlug:null==j?void 0:j.nickname}),children:(0,n.jsx)(V.r,{href:h?(0,W.U)({userSlug:null==j?void 0:j.nickname}):(0,Q.d)({userSlug:null==j?void 0:j.nickname}),as:d&&j?V.r:S.O,onClick:()=>{Z.lC.onClose(),Z._9.onClose()},children:(0,n.jsxs)(M.U,{children:[(0,n.jsx)(z.Y,{size:"xs",avatarAlt:null==j?void 0:j.nickname,avatarUrl:null==j?void 0:j.avatarUrl}),(0,n.jsx)(F.x,{noOfLines:1,fontSize:"12px",maxW:"calc(100% - 70px)",children:null==j?void 0:j.nickname}),!!(null==j?void 0:j.verifiedSubscriptionTier)&&(0,n.jsx)(G.X,{tier:j.verifiedSubscriptionTier,size:16})]})})}),(null==f?void 0:f.university.name)&&(0,n.jsxs)(M.U,{children:[(0,n.jsx)(Y.M,{children:(0,n.jsx)($.W4t,{size:"24px"})}),(0,n.jsx)(F.x,{children:null==f?void 0:f.university.name})]}),(0,n.jsxs)(M.U,{as:!d||m.isLoading?S.O:M.U,children:[(0,n.jsx)(Y.M,{children:(0,n.jsx)($.W4t,{size:"24px"})}),(0,n.jsx)(F.x,{children:null==f?void 0:f.name})]}),(0,n.jsxs)(M.U,{as:!d||x.isLoading?S.O:M.U,children:[(0,n.jsx)(Y.M,{children:(0,n.jsx)($._xL,{size:"24px"})}),(0,n.jsx)(F.x,{children:null==w?void 0:w.name})]}),(0,n.jsxs)(M.U,{as:d?M.U:S.O,children:[(0,n.jsx)(Y.M,{children:(0,n.jsx)($.TzF,{size:"24px"})}),(0,n.jsx)(F.x,{children:d?(0,H.SC)(d.createdAt):""})]})]})},DocumentPopover=e=>{let{document:o,children:t,eventFunction:l}=e,{t:s}=(0,r.qM)(),{isMobile:T}=(0,m.B)(),M=(0,x.q)(),{data:F}=(0,u.Pi)(),S=!!F&&F.id===(null==o?void 0:o.userId),L=!!F&&2===F.role;return T?(0,n.jsxs)(n.Fragment,{children:[d.Children.map(t,(e,o)=>(0,d.isValidElement)(e)&&0===o?(0,d.cloneElement)(e,{onClick:e=>{e.stopPropagation(),l&&l(),M.onOpen()}}):e),M.isOpen&&(0,n.jsxs)(h.u_,{isOpen:M.isOpen,onClose:M.onClose,children:[(0,n.jsx)(f.Z,{}),(0,n.jsxs)(w.h,{mt:"auto",mb:"0",children:[(0,n.jsx)(j.o,{}),(0,n.jsx)(v.f,{children:(0,n.jsxs)(y.K,{children:[(0,n.jsx)(Info,{document:o}),(0,n.jsx)(g.i,{py:"2"}),!!(null==F?void 0:F.isPro)&&(0,n.jsxs)(n.Fragment,{children:[(0,n.jsx)(LegacyQuickDownloadButton,{document:o,location:"options menu mobile"}),(0,n.jsx)(P.d,{document:o,location:"options menu mobile"})]}),!(null==F?void 0:F.isPro)&&!!o&&(0,n.jsx)(p.l,{document:o}),(0,n.jsx)(E.k$,{location:"options menu mobile",document:o}),!!o&&(0,n.jsx)(c.v,{document:o}),!S&&(0,n.jsx)(A.f,{eventFunction:()=>{(0,i.G)({location:"options menu mobile",button:"report"})},report:{variant:a.t.DOCUMENT,item:o}}),L&&o&&(0,n.jsxs)(n.Fragment,{children:[(0,n.jsx)(U.Qm,{documentId:o.id}),(0,n.jsx)(B.qc,{documentId:o.id})]})]})})]})]})]}):(0,n.jsxs)(D.J,{placement:"left-start",isLazy:!0,children:[(0,n.jsx)(b.x,{children:t}),(0,n.jsxs)(C.y,{w:"280px",children:[(0,n.jsx)(k.Y,{fontSize:"12px",px:"4",pt:"4",pb:"3",children:s("info-title")}),(0,n.jsx)(_.b,{borderRadius:"md",px:"4",pt:"3",pb:"4",children:(0,n.jsxs)(y.K,{children:[(0,n.jsx)(Info,{document:o}),(0,n.jsx)(I.c,{my:"2"}),!!(null==F?void 0:F.isPro)&&(0,n.jsxs)(n.Fragment,{children:[(0,n.jsx)(LegacyQuickDownloadButton,{document:o,location:"options menu desktop"}),(0,n.jsx)(P.d,{document:o,location:"options menu desktop"})]}),!(null==F?void 0:F.isPro)&&!!o&&(0,n.jsx)(p.l,{document:o}),(0,n.jsx)(E.k$,{location:"options menu desktop",document:o}),!!o&&(0,n.jsx)(c.v,{document:o}),!S&&(0,n.jsx)(A.f,{eventFunction:()=>{(0,i.G)({location:"options menu desktop",button:"report"})},report:{variant:a.t.DOCUMENT,item:o},"data-testid":"report-button"}),L&&o&&(0,n.jsxs)(n.Fragment,{children:[(0,n.jsx)(U.Qm,{documentId:o.id}),(0,n.jsx)(B.qc,{documentId:o.id})]})]})})]})]})};var Header_InfoIconButton=e=>{let{document:o,location:t,...a}=e,{t:d}=(0,r.qM)();return(0,n.jsx)(DocumentPopover,{eventFunction:()=>{(0,i.G)({location:t,button:"options menu"})},document:o,children:(0,n.jsx)(l.h,{icon:(0,n.jsx)(s.r0I,{}),"aria-label":d("info-button"),title:d("info-button"),onClick:()=>{(0,i.G)({location:t,button:"options menu"})},...a,"data-testid":"options-menu-document-button"})})}},49454:function(e,o,t){"use strict";t.d(o,{Z:function(){return ProBadge}});var n=t(52322),r=t(4036);let ProBadge=e=>{let{...o}=e;return(0,n.jsx)(r.C,{colorScheme:"pro",fontSize:"8px",h:"12px",px:"4px",display:"flex",alignItems:"center",justifyContent:"center",fontWeight:"400",borderRadius:"full",textTransform:"uppercase",...o,children:"Pro"})}},98850:function(e,o,t){"use strict";t.d(o,{G:function(){return documentClicked}});var n=t(91330),r=t(79045);let i="document_clicked",documentClicked=e=>{var o;let{location:t,button:l}=e;null===r.h||void 0===r.h||null===(o=r.h.dataLayer)||void 0===o||o.push({event:i,location:t,button:l}),n.hotjar.initialized()&&n.hotjar.event("".concat(i,":").concat(t,":").concat(l))}},75191:function(e,o,t){"use strict";t.d(o,{u:function(){return folderClicked}});var n=t(91330),r=t(79045);let i="folder_clicked",folderClicked=e=>{var o;let{location:t,button:l}=e;null===r.h||void 0===r.h||null===(o=r.h.dataLayer)||void 0===o||o.push({event:i,location:t,button:l}),n.hotjar.initialized()&&n.hotjar.event("".concat(i,":").concat(t,":").concat(l))}},44354:function(e,o,t){"use strict";t.d(o,{O:function(){return artifactClicked}});var n=t(98850),r=t(75191);let artifactClicked=e=>{let{locationType:o,location:t,button:i}=e;switch(o){case"document":(0,n.G)({location:t,button:i});break;case"folder":(0,r.u)({location:t,button:i})}}},24642:function(e,o,t){"use strict";t.d(o,{Y:function(){return DocumentShareIconButton},v:function(){return DocumentShareButton}});var n=t(52322),r=t(69316),i=t(26488),l=t(25951),s=t(98317);let DocumentShareButton=e=>{let{document:o}=e,{caption:t,icon:l,showSkeleton:a,onClick:d}=(0,s.y)({document:o});return(0,n.jsx)(r.z,{leftIcon:l,onClick:d,...a?{as:i.O}:{},children:t})},DocumentShareIconButton=e=>{let{document:o}=e,{caption:t,icon:r,showSkeleton:a,onClick:d}=(0,s.y)({document:o});return(0,n.jsx)(l.h,{icon:r,onClick:d,"aria-label":t,...a?{as:i.O}:{}})}},89176:function(e,o,t){"use strict";t.d(o,{M:function(){return GroupDownloadProgressToast}});var n,r,i,l,s=t(52322),a=t(31171),d=t(66946),u=t(2784),c=t(25951),p=t(420),m=t(951),x=t(52742),h=t(45092),f=t.n(h);(n=i||(i={})).IN_PROGRESS="inProgress",n.COMPLETED="completed",n.ERROR="error";let ProgressToast=e=>{let o,{filledPercent:t,status:n="inProgress",title:r="",description:i="",onClose:l}=e,a=f().wrapper,d=f().fillingBackground;switch(n){case"completed":a+=" ".concat(f().completed),d+=" ".concat(f().completed);break;case"error":o=(0,s.jsx)(c.h,{"aria-label":"reintentar",icon:(0,s.jsx)(m.Em2,{}),size:"xs",color:"#ffffff",backgroundColor:"#ff2200"}),a+=" ".concat(f().error),d+=" ".concat(f().error);break;default:o=(0,s.jsx)(p.$,{width:"16px",height:"16px",color:"#0055ff"}),a+=" ".concat(f().inProgress),d+=" ".concat(f().inProgress)}return(0,s.jsx)("div",{className:a,children:(0,s.jsxs)("div",{className:f().toast,children:[(0,s.jsx)("div",{className:d,style:{width:"".concat(t,"%")}}),(0,s.jsxs)("div",{className:f().main,children:[(0,s.jsx)("p",{className:f().title,title:r,children:r}),(0,s.jsx)("p",{className:f().description,children:i}),(0,s.jsxs)("p",{className:f().progressText,children:[t.toFixed(0),"%"]})]}),(0,s.jsxs)("div",{className:f().side,children:[(0,s.jsx)(x.apv,{color:"#6a6a6a",cursor:"pointer",onClick:()=>{l&&l()}}),"error"===n&&(0,s.jsx)(x.Von,{color:"#dbdbdb"}),o]})]})})};var w=t(28381),j=t(82066),v=t(12825);let deliverGroupDownload=async e=>{let{groupDownloadId:o}=e,t=await (0,v._i)("/v2/group-downloads/".concat(o,"/deliver"),{method:"PUT"});return t},useDeliverGroupDownload=()=>(0,j.useMutation)(deliverGroupDownload);(r=l||(l={}))[r.RECHECK_TIME=1e3]="RECHECK_TIME";let GroupDownloadProgressToast=e=>{let{groupDownloadId:o,downloadName:t,onDownloadSuccess:n,onClose:r}=e,{t:l}=(0,a.qM)(),[c,p]=(0,u.useState)(0),[m,x]=(0,u.useState)(i.IN_PROGRESS),[h,f]=(0,u.useState)(!1),[j,v]=(0,u.useState)(!1),{mutateAsync:y}=useDeliverGroupDownload();(0,u.useEffect)(()=>{f(!0)},[]),(0,u.useEffect)(()=>{let downloadGroup=async()=>{let e=d.eK.REQUESTED;for(;[d.eK.REQUESTED,d.eK.PROCESSING,d.eK.COMPLETED].includes(e);){let r=await y({groupDownloadId:o});if(r.status===d.eK.DELIVERED){x(i.COMPLETED),p(100),v(!0),await (0,w.j)({url:r.url,name:t}),n&&n();break}if(r.status===d.eK.FAILED){x(i.ERROR),v(!0);break}e=r.status,p(r.progress),await new Promise(e=>setTimeout(e,1e3))}};h&&!j&&downloadGroup()},[o,t,h,j,y,n]);let g=l("group-download.creating");return m===i.COMPLETED&&(g=l("group-download.completed")),m===i.ERROR&&(g=l("group-download.error")),(0,s.jsx)(ProgressToast,{filledPercent:c,status:m,title:t,description:g,onClose:r})}},51106:function(e,o,t){"use strict";t.d(o,{j:function(){return FolderDownloadDrawer}});var n=t(52322),r=t(31171),i=t(78116),l=t(56915),s=t(16796),a=t(92619),d=t(91012),u=t(64827),c=t(2167),p=t(9890);let FolderDownloadDrawer=e=>{let{upload:o,isOpen:t,onClose:m}=e,{t:x}=(0,r.qM)();return(0,n.jsxs)(i.d,{isOpen:t,onClose:m,placement:"bottom",children:[(0,n.jsx)(l.Z,{}),(0,n.jsxs)(s.s,{borderTopRadius:"8px",children:[(0,n.jsxs)(a.x,{padding:"16px",borderBottom:"1px solid #ededed",children:[(0,n.jsx)(d.x,{fontSize:"14px",lineHeight:"21px",fontWeight:"600",children:x("folder-download-button")}),(0,n.jsx)(u.o,{top:"15px"})]}),(0,n.jsx)(c.f,{padding:"0",children:(0,n.jsx)(p.Y,{upload:o})})]})]})}},9890:function(e,o,t){"use strict";t.d(o,{Y:function(){return FolderDownloadModalBody}});var n=t(52322),r=t(31171),i=t(66946),l=t(5261),s=t(67423),a=t(26488),d=t(65082),u=t(8757),c=t(91868),p=t(52742);let CoinsFolderDownloadButton=e=>{let{upload:o,downloadPrice:t}=e,{t:i}=(0,r.qM)(),{onClick:l,isLoading:s}=(0,c.p)({upload:o});return(0,n.jsx)(u.g,{variant:"filled",leftIcon:(0,n.jsx)(p.JKW,{size:"18px"}),rightIcon:(0,n.jsx)(d.a,{isPrimary:!0,coins:t.amount}),disabled:s,loading:s,onClick:l,children:i("word-download")})};var m=t(94395),x=t.n(m),h=t(2784),f=t(20811),w=t(94853),j=t(38348),v=t(26021),y=t(60489);let useFreeFolderDownloadAction=e=>{let{upload:o,onDownloadSuccess:t}=e,{t:n}=(0,r.qM)(),{folderDocuments:i=[]}=(0,f.W)({uploadId:null==o?void 0:o.id}),{data:s}=(0,l.CN)(),{mutateAsync:a}=(0,j.B)(),{askEmailConfirm:d}=(0,w._)(),[u,c]=(0,h.useState)(!1),handleClick=async()=>{if(!s)return y.Lq.onOpen();let{isConfirmed:e}=d();if(e){c(!0);try{await a({documents:i,name:o.name}),t&&t()}catch(e){console.error(e),(0,v.A)({title:n("error"),description:n("error-occurred"),status:"error"})}finally{c(!1)}}};return{onClick:handleClick,isLoading:u}},FreeFolderDownloadButton=e=>{let{upload:o,onDownloadSuccess:t}=e,{t:i}=(0,r.qM)(),{onClick:l,isLoading:s}=useFreeFolderDownloadAction({upload:o,onDownloadSuccess:t});return(0,n.jsx)(u.g,{variant:"outlined",leftIcon:(0,n.jsx)(p.JKW,{size:"18px"}),onClick:l,disabled:s,loading:s,children:i("word-download")})},FolderDownloadModalBody=e=>{let{upload:o}=e,{t}=(0,r.qM)(),{data:d}=(0,l.Pi)(),{data:u}=(0,s.v)({enabled:!!d&&!!o,origin:{type:i.XT.FOLDER,id:o.id},isPro:!!(null==d?void 0:d.isPro)}),c="".concat("https://cdn.wuolahservices.com","/pro/");switch(null==d?void 0:d.subscriptionTier){case i.DW.TIER_2:c+="planProIcon.png";break;case i.DW.TIER_3:default:c+="planProPlusIcon.png"}return(0,n.jsxs)("div",{children:[(0,n.jsxs)("div",{className:x().row,children:[(0,n.jsxs)("div",{className:x().iconWrapper,children:[(0,n.jsx)("img",{src:c,alt:"",className:x().icon}),(0,n.jsx)("img",{alt:"",src:"".concat("https://cdn.wuolahservices.com","/coins/coinIcon.png"),className:x().coin})]}),(0,n.jsxs)("div",{children:[(0,n.jsx)("p",{className:x().title,children:t("download-without-ads")}),(0,n.jsx)("p",{className:x().description,children:(0,n.jsx)(r.T,{keyName:"user.available-coins",params:{coins:(null==d?void 0:d.coins)||"0"}})})]}),u?(0,n.jsx)(CoinsFolderDownloadButton,{upload:o,downloadPrice:u}):(0,n.jsx)(a.O,{width:"100%",height:"16px"})]}),(0,n.jsx)("div",{className:x().divider}),(0,n.jsxs)("div",{className:x().row,children:[(0,n.jsx)("div",{className:x().iconWrapper,children:(0,n.jsx)("img",{src:c,alt:"",className:x().icon})}),(0,n.jsx)("p",{className:x().title,children:t("download-with-ads")}),(0,n.jsx)(FreeFolderDownloadButton,{upload:o})]})]})}},91868:function(e,o,t){"use strict";t.d(o,{p:function(){return useCoinsFolderDownloadAction}});var n=t(31171),r=t(66946),i=t(5632),l=t(94853),s=t(5261),a=t(65242),d=t(44683),u=t(9477),c=t(19519),p=t(92251),m=t(60489),x=t(59519),h=t(57569);let useCoinsFolderDownloadAction=e=>{let{upload:o,onDownloadSuccess:t}=e,{t:f}=(0,n.qM)(),{push:w}=(0,i.useRouter)(),{data:j}=(0,s.Pi)(),{data:v}=(0,a.p)(),{data:y}=(0,d.Mb)({currency:(null==v?void 0:v.currency)||r.wA.EUR,enabled:!!j&&!(0,u.HR)(null==j?void 0:j.subscriptionTier)}),{download:g,isLoading:D}=(0,h.n)(),{askEmailConfirm:b}=(0,l._)(),C=c.H.DOWNLOAD_FOLDER_BUTTON,handleClick=async()=>{if(!j){m.Lq.onOpen();return}let{isConfirmed:e}=b();if(e){if(!(0,u.HR)(j.subscriptionTier)){m._9.onClose();let e=null==y?void 0:y.prices.find(e=>e.interval===r.oL.MONTHLY);if(!e||!y)return;(0,p.TQ)({product:y,price:e,listName:C,component:"useCoinsFolderDownloadAction"}),w((0,x.m)({tab:"pro",listName:C}));return}await g({upload:o,withAds:!1,onSuccess:t})}};return{onClick:handleClick,isLoading:D}}},57569:function(e,o,t){"use strict";t.d(o,{n:function(){return useFolderDownload}});var n=t(52322),r=t(31171),i=t(82066),l=t(5261),s=t(89176),a=t(26021),d=t(18714),u=t(60489),c=t(19841),p=t(12825);let m={noCoins:"GD004"},createFolderGroupDownload=async e=>{let{params:{uploadId:o},body:t}=e,n=await (0,p._i)("/v2/group-downloads/uploads/".concat(o),{method:"POST",body:JSON.stringify(t)});return n},useCreateFolderGroupDownload=()=>(0,i.useMutation)(createFolderGroupDownload),useFolderDownload=()=>{let{t:e}=(0,r.qM)(),o=(0,i.useQueryClient)(),{data:t}=(0,l.Pi)(),{mutateAsync:p,isLoading:x}=useCreateFolderGroupDownload(),h=(0,d._)(),handleDownload=async r=>{let{upload:l,withAds:d,onSuccess:x}=r;if(!t)return;let f=await (0,c.s)(t.captchaCounter),w=null;try{w=await p({params:{uploadId:l.id},body:{withAds:d,machineId:h,captchaCode:f}})}catch(e){if(e&&e.key===m.noCoins){u.bM.onOpen();return}throw e}if(!w){(0,a.A)({title:e("error"),description:e("error-occurred"),status:"error"});return}let j=(0,a.A)({duration:null,render:()=>(0,n.jsx)(i.QueryClientProvider,{client:o,children:(0,n.jsx)(s.M,{groupDownloadId:(null==w?void 0:w.id)||"",downloadName:"wuolah-premium-".concat(l.name,".zip"),onDownloadSuccess:()=>{x&&x(),setTimeout(()=>{a.A.close(j)},5e3)},onClose:()=>{a.A.close(j)}})})})};return{download:handleDownload,isLoading:x}}},13651:function(e,o,t){"use strict";t.d(o,{J:function(){return usePopoverFolderDownloadAction}});var n=t(66946),r=t(5632),i=t(94853),l=t(5261),s=t(65242),a=t(44683),d=t(9477),u=t(19519),c=t(92251),p=t(60489),m=t(59519),x=t(75944);let usePopoverFolderDownloadAction=()=>{let{isOpen:e,onClose:o,onToggle:t}=(0,x.q)(),h=(0,r.useRouter)(),{data:f}=(0,l.Pi)(),{data:w}=(0,s.p)(),{data:j}=(0,a.n4)({currency:(null==w?void 0:w.currency)||n.wA.EUR,enabled:!!f&&!(0,d.HR)(null==f?void 0:f.subscriptionTier)}),v=null==j?void 0:j.prices.find(e=>e.interval===n.oL.MONTHLY),{askEmailConfirm:y}=(0,i._)(),g=u.H.DOWNLOAD_FOLDER_BUTTON;return(0,c.x1)({enabled:!!j,listName:g,component:"usePopoverFolderDownloadAction",products:(null==j?void 0:j.prices.filter(e=>e.interval===n.oL.MONTHLY).map(e=>({product:j,price:e})))||[]}),{onClick:()=>{if(!f){p.Lq.onOpen();return}let{isConfirmed:e}=y();if(e){if(!(0,d.HR)(f.subscriptionTier)){if(p._9.onClose(),!j||!v)return;(0,c.TQ)({product:j,price:v,listName:g,component:"usePopoverFolderDownloadAction"}),h.push((0,m.m)({tab:"pro",listName:g}));return}t()}},isOpen:e,onClose:o}}},38348:function(e,o,t){"use strict";t.d(o,{B:function(){return useDownloadZippedDocuments}});var n=t(31171),r=t(5632),i=t(82066),l=t(88694),s=t(71149),a=t(44926),d=t(94853),u=t(5261),c=t(9477),p=t(26021),m=t(1297),x=t.n(m);let compressFiles=async e=>{let{name:o,files:t}=e,n=new(x()),r=n.folder(o),i=await Promise.all(t.map(async e=>{let{url:o,name:t}=e,n=await fetch(o);return 200===n.status?{blob:await n.blob(),name:t}:Promise.reject(Error(n.statusText))}));return i.forEach(e=>{let{blob:o,name:t}=e;null==r||r.file(t,o)}),n};var h=t(46782);let downloadFileBlob=async e=>{let{name:o,blob:t}=e;return(0,h.saveAs)(t,o)};var f=t(15757);let downloadZip=async e=>{let{name:o,premium:t,zip:n}=e;return n.generateAsync({type:"blob"}).then(e=>{try{return downloadFileBlob({name:(0,f.U)({name:o,premium:t,mimeType:"zip"}),blob:e})}catch(e){console.error(e)}})};var w=t(69544),j=t(52322),v=t(62202),y=t(8283),g=t(71354),D=t(66724),b=t(18751),C=t(72360),k=t(16844),_=t(420),I=t(87618);function FolderName(e){let{title:o,subtitle:t}=e;return(0,j.jsxs)(y.U,{w:"full",children:[(0,j.jsx)(I.R,{}),(0,j.jsxs)(g.K,{w:"full",spacing:"0",children:[(0,j.jsx)(D.C,{noOfLines:1,children:o}),(0,j.jsx)(b.X,{noOfLines:1,children:t})]})]})}let downloadingDocumentsToast=e=>{let{name:o,files:{downloaded:t,total:r},...i}=e;return v.ZP.custom(e=>(0,j.jsx)(C.b,{status:"info",w:"320px",children:(0,j.jsxs)(g.K,{w:"full",children:[(0,j.jsx)(FolderName,{title:o,subtitle:(0,j.jsx)(n.T,{keyName:"folder-downloading-files-toast",params:{current:t,total:r}})}),(0,j.jsx)(k.E,{min:0,max:r,value:t,hasStripe:!0,isAnimated:!0})]})}),{position:"bottom-right",duration:1/0,...i})},compressingDocumentsToast=e=>{let{name:o,...t}=e;return v.ZP.custom(e=>(0,j.jsx)(C.b,{status:"info",w:"320px",children:(0,j.jsxs)(y.U,{w:"full",children:[(0,j.jsx)(FolderName,{title:o,subtitle:(0,j.jsx)(n.T,{keyName:"folder-compressing-toast"})}),(0,j.jsx)(_.$,{})]})}),{position:"bottom-right",duration:1/0,...t})},downloadingZipToast=e=>{let{name:o,...t}=e;return v.ZP.custom(e=>(0,j.jsx)(C.b,{status:"info",w:"320px",children:(0,j.jsxs)(y.U,{w:"full",children:[(0,j.jsx)(FolderName,{title:o,subtitle:(0,j.jsx)(n.T,{keyName:"folder-downloading-toast"})}),(0,j.jsx)(_.$,{})]})}),{position:"bottom-right",duration:1/0,...t})},downloadedZipToast=e=>{let{name:o,files:{downloaded:t,total:r},...i}=e;return v.ZP.custom(e=>(0,j.jsx)(C.b,{status:"success",w:"320px",children:(0,j.jsx)(FolderName,{title:o,subtitle:(0,j.jsx)(n.T,{keyName:"folder-downloaded-toast",params:{current:t,total:r}})})}),{position:"bottom-right",duration:5e3,...i})},errorDownloadingZipToast=e=>{let{name:o,error:t,...r}=e;return v.ZP.custom(e=>(0,j.jsx)(C.b,{status:"error",w:"320px",children:(0,j.jsx)(FolderName,{title:o,subtitle:(0,j.jsx)(n.T,{keyName:t})})}),{position:"bottom-right",duration:5e3,...r})};var B=t(28422);let useDownloadZippedDocuments=()=>{var e,o;let{t}=(0,n.qM)(),{data:m}=(0,u.Pi)(),{mutateAsync:x}=(0,s.iJ)(),{askAuth:h}=(0,a.S)(),{askEmailConfirm:j}=(0,d._)(),v=(0,B.Me)(),{query:y}=(0,r.useRouter)(),g=null!==(o=null===(e=y.referral)||void 0===e?void 0:e.toString())&&void 0!==o?o:"";return(0,i.useMutation)(async e=>{let{name:o,documents:n,noAdsToken:r,withCoins:i=!1}=e,{isAuth:a}=h();if(!a)return Promise.reject();let{isConfirmed:d}=j();if(!d)return Promise.reject();if(!(0,c.HR)(null==m?void 0:m.subscriptionTier))return(0,p.A)({title:t("error-occurred"),description:t("folder-download-error-subscribe"),status:"error"}),Promise.reject();try{let e=0;downloadingDocumentsToast({id:v,name:o,files:{downloaded:e,total:n.length}});let t=await Promise.all(n.map(async(t,a)=>{try{let l=await x({document:t,referralCode:g,withCoins:i,noAdsToken:r});return e+=1,downloadingDocumentsToast({id:v,name:o,files:{downloaded:e,total:n.length}}),{url:l.url,name:(0,f.U)({folderIndex:a+1,name:t.name,premium:!!i,mimeType:t.extension.original})}}catch(e){if(e instanceof l.S&&e.key===s.uf.downloadLimit)throw new l.S({code:e.key,message:e.message},e.status||500);return}}));compressingDocumentsToast({id:v,name:o});let a=await compressFiles({name:o,files:t.filter(w.D)});downloadingZipToast({id:v,name:o}),await downloadZip({name:o,premium:i,zip:a}),downloadedZipToast({id:v,name:o,files:{downloaded:e,total:n.length}})}catch(e){if(!(e instanceof l.S))return;e.key===s.uf.downloadLimit?errorDownloadingZipToast({id:v,name:o,error:e.key,duration:100}):errorDownloadingZipToast({id:v,name:o,error:e.key})}})}},67423:function(e,o,t){"use strict";t.d(o,{v:function(){return useFindGroupDownloadPrice}});var n=t(82066),r=t(12825),i=t(15442),l=t(548);let findGroupDownloadPriceKey=e=>{let{origin:o,isPro:t}=e;return[{id:"find-group-download-price",origin:o,isPro:t}]},findGroupDownloadPrice=async e=>{let{queryKey:[{origin:o,isPro:t}]}=e,n=(0,i.P)({origin:o,isPro:t}),l=await (0,r._i)("/v2/group-downloads/price".concat(n));return l},useFindGroupDownloadPrice=e=>{let{enabled:o,origin:t,isPro:r}=e,i=o&&!!t&&void 0!==r;return(0,n.useQuery)({queryKey:(0,l.I)(findGroupDownloadPriceKey({origin:t,isPro:r}),i),queryFn:findGroupDownloadPrice,enabled:i})}},74587:function(e,o,t){"use strict";t.d(o,{s:function(){return MenuItem}});var n=t(52322),r=t(96012),i=t(8283),l=t(91012),s=t(24763);let MenuItem=e=>{let{icon:o,label:t,badge:a,onClick:d,color:u,isNew:c=!1,dataTestId:p}=e;return(0,n.jsx)(r.s,{icon:o,color:u,onClick:d,"data-testid":p,children:(0,n.jsxs)(i.U,{justifyContent:"space-between",children:[(0,n.jsx)(l.x,{children:t}),a,c&&(0,n.jsx)(s.P,{})]})})}},19841:function(e,o,t){"use strict";t.d(o,{s:function(){return handleCaptcha}});var n=t(24732);let handleCaptcha=async e=>{if(!(e>0))try{let{askCaptcha:e}=(0,n.d)(),o=await e();return o}catch(e){}}},94395:function(e){e.exports={row:"FolderDownloadModalBody_row__m8HVm",iconWrapper:"FolderDownloadModalBody_iconWrapper__T5xFA",icon:"FolderDownloadModalBody_icon__xWRcT",coin:"FolderDownloadModalBody_coin__sheE3",title:"FolderDownloadModalBody_title__zKhBM",description:"FolderDownloadModalBody_description__s5t3B",divider:"FolderDownloadModalBody_divider__93ic4"}},45092:function(e){e.exports={wrapper:"ProgressToast_wrapper__lJZ7k",inProgress:"ProgressToast_inProgress__btMQ_",completed:"ProgressToast_completed___0kqH",error:"ProgressToast_error__AWOjC",toast:"ProgressToast_toast__Gnn9o",fillingBackground:"ProgressToast_fillingBackground__z2tou",main:"ProgressToast_main__MWIXv",title:"ProgressToast_title__cBrmc",description:"ProgressToast_description__AJBcW",progressText:"ProgressToast_progressText__H7rI7",side:"ProgressToast_side__ZLTZl",LoopingProgressBar:"ProgressToast_LoopingProgressBar__21ca6"}}}]);